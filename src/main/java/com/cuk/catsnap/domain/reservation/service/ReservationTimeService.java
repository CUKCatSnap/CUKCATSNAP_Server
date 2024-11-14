package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeListResponse;
import com.cuk.catsnap.domain.reservation.dto.member.response.PhotographerAvailableReservationTimeResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.request.ReservationTimeFormatRequest;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatIdResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatListResponse;
import com.cuk.catsnap.domain.reservation.dto.photographer.response.ReservationTimeFormatResponse;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ReservationRepository reservationRepository;
    private final WeekdayService weekdayService;

    public PhotographerAvailableReservationTimeListResponse getAvailableReservationTime(
        LocalDate date, Long photographerId) {
        Weekday weekday = weekdayService.getWeekday(date);
        String ReservationTimeFormatId = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographerId, weekday)
            .orElseThrow(() -> new OwnershipNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."))
            .getReservationTimeFormatId();
        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(
            ReservationTimeFormatId);
        if (reservationTimeFormat == null) {
            return PhotographerAvailableReservationTimeListResponse.from(Collections.emptyList());
        }

        /*
         * photographerStartTimeList는 작가가 설정한 예약 가능한 시간 목록입니다.(단 현재 시간 이후만 조회)
         * ReservationList는 현재까지 작가에게 예약된 예약 목록입니다.
         * 현재 예약 가능한 시간대를 조회하기 위해 예약된 시간대를 isAvailableReservation을 false로 설정하여 반환합니다.
         */
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList()
            .stream()
            .filter(time -> time.atDate(date).isAfter(LocalDateTime.now()))
            .toList();
        List<Reservation> reservationList = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(
            photographerId, LocalDateTime.of(date, LocalTime.MIN),
            LocalDateTime.of(date, LocalTime.MAX));

        List<PhotographerAvailableReservationTimeResponse> photographerAvailableReservationTimeResponseArrayList = new ArrayList<>();
        for (LocalTime startTime : photographerStartTimeList) {
            boolean isAvailableReservation = true;
            LocalDateTime startDateTime = LocalDateTime.now().toLocalDate().atTime(startTime);
            for (Reservation reservation : reservationList) {
                LocalDateTime reservationStartTime = reservation.getStartTime();
                LocalDateTime reservationEndTime = reservation.getEndTime();
                if (reservationStartTime.isAfter(startDateTime)
                    || reservationStartTime.isEqual(startDateTime)
                    && reservationEndTime.isBefore(startDateTime) || reservationEndTime.isEqual(
                    startDateTime)) {
                    isAvailableReservation = false;
                    break;
                }
            }
            photographerAvailableReservationTimeResponseArrayList.add(
                PhotographerAvailableReservationTimeResponse.of(startTime, isAvailableReservation));
        }

        return PhotographerAvailableReservationTimeListResponse.from(
            photographerAvailableReservationTimeResponseArrayList);
    }

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
