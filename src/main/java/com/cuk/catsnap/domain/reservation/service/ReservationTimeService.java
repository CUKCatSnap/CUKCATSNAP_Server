package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.photographer.request.ReservationTimeFormatRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatIdResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatListResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatResponse;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationTimeService {

    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;

    public ReservationTimeFormatIdResponse createReservationTimeFormat(
        ReservationTimeFormatRequest reservationTimeFormatRequest, String reservationTimeFormatId) {

        Long photographerId = GetAuthenticationInfo.getUserId();
        ReservationTimeFormat reservationTimeFormat = null;

        /*
         *reservationTimeFormatId가 null이면 새로운 ReservationTimeFormat 생성이고,
         * null이 아니라면 기존의 ReservationTimeFormat 업데이트.
         */
        if (reservationTimeFormatId == null) {
            reservationTimeFormat = reservationTimeFormatRequest.toEntity(photographerId);
            reservationTimeFormatRepository.save(reservationTimeFormat);
        } else {
            reservationTimeFormat = reservationTimeFormatRequest.toEntity(reservationTimeFormatId,
                photographerId);
            UpdateResult updateResult = reservationTimeFormatRepository.update(
                reservationTimeFormat);
            if (updateResult.getModifiedCount() == 0) {
                throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
            }
        }
        return ReservationTimeFormatIdResponse.from(reservationTimeFormat);
    }

    public ReservationTimeFormatListResponse getMyReservationTimeFormatList() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        List<ReservationTimeFormat> reservationTimeFormatList = reservationTimeFormatRepository.findByPhotographerId(
            photographerId);
        return ReservationTimeFormatListResponse.from(
            reservationTimeFormatList.stream()
                .map(ReservationTimeFormatResponse::from)
                .toList());
    }

    public void deleteReservationTimeFormat(String reservationTimeFormatId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        DeleteResult deleteResult = reservationTimeFormatRepository.deleteById(
            reservationTimeFormatId, photographerId);
        if (deleteResult.getDeletedCount() == 0) {
            throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
        }
        // 해당 time format을 이용하고 있는 요일의 매핑을 null로 초기화
        weekdayReservationTimeMappingRepository.updateReservationTimeFormatIdToNull(photographerId,
            reservationTimeFormatId);
    }

    /*
     * 요일을 예약 시간 형식에 매핑시킨다. (요일을 고정. 예약 시간을 요일에 매핑시키는 것이다.)
     */
    public void mappingWeekdayToReservationTimeFormat(String reservationTimeFormatId,
        Weekday weekday) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Optional<WeekdayReservationTimeMapping> weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
            photographerId, weekday);
        weekdayReservationTimeMapping.ifPresentOrElse(mapping ->
                mapping.updateReservationTimeFormatId(reservationTimeFormatId),
            () -> new OwnershipNotFoundException("내가 소유한 요일 중, 해당 요일을 찾을 수 없습니다.")
        );
    }

    public void unmappingWeekdayToReservationTimeFormatByWeekday(Weekday weekday) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        Optional<WeekdayReservationTimeMapping> weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
            photographerId, weekday);
        weekdayReservationTimeMapping.ifPresentOrElse(mapping ->
                mapping.updateReservationTimeFormatId(null),
            () -> new OwnershipNotFoundException("내가 소유한 요일 중, 해당 요일을 찾을 수 없습니다.")
        );
    }
}
