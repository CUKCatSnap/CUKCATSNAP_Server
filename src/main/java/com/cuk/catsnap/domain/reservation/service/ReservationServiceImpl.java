package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.converter.ReservationConverter;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import com.cuk.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.security.contextholder.GetAuthenticationInfo;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final ReservationConverter reservationConverter;

    /*
    * 새로운 작가가 회원가입을 하면, 각 요일에 대한 예약 테이블을 생성한다.
    * 작가는 예약 시간 형식을 만들 수 있고, 이 형식을 특정 요일에 매핑시켜야 한다.
    * 따라서 작가의 예약 시간 형식을 요일에 매핑시키기 위해 요일 - 형식 테이블을 생성한다.
     */
    public void createJoinedPhotographerReservationTimeFormat(Photographer photographer) {
        //추후 벌크 삽입으로 변경 예정
        List<WeekdayReservationTimeMapping> weekdayReservationTimeMappingList
                = Arrays.stream(Weekday.values())
                        .map(weekday ->WeekdayReservationTimeMapping.builder()
                                .photographer(photographer)
                                .weekday(weekday)
                                .build()
                        )
                        .toList();
        weekdayReservationTimeMappingRepository.saveAll(weekdayReservationTimeMappingList);
    }

    @Override
    public String createReservationTimeFormat(ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat, String reservationTimeFormatId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        ReservationTimeFormat reservationTimeFormat = reservationConverter.toReservationTimeFormat(photographerReservationTimeFormat, photographerId);
        /*
        *reservationTimeFormatId가 null이면 새로운 ReservationTimeFormat 생성이고,
        * null이 아니라면 기존의 ReservationTimeFormat 업데이트.
         */
        if(reservationTimeFormatId == null) {
            return reservationTimeFormatRepository.save(reservationTimeFormat).getId();
        } else {
            UpdateResult updateResult=  reservationTimeFormatRepository.update(reservationTimeFormat, reservationTimeFormatId, photographerId);
            if(updateResult.getModifiedCount() == 0) {
                throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
            }
            return reservationTimeFormatId;
        }
    }

    @Override
    public List<ReservationTimeFormat> getMyReservationTimeFormatList() {
        Long photographerId = GetAuthenticationInfo.getUserId();
        return reservationTimeFormatRepository.findByPhotographerId(photographerId);
    }

    @Override
    public void deleteReservationTimeFormat(String reservationTimeFormatId) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        DeleteResult deleteResult = reservationTimeFormatRepository.deleteById(reservationTimeFormatId,photographerId);
        if(deleteResult.getDeletedCount() == 0) {
            throw new OwnershipNotFoundException("내가 소유한 예약 시간 형식 중, 해당 예약 시간 형식을 찾을 수 없습니다.");
        }
        // 해당 time format을 이용하고 있는 요일의 매핑을 null로 초기화
        weekdayReservationTimeMappingRepository.updateReservationTimeFormatIdToNull(photographerId,reservationTimeFormatId);
    }
}
