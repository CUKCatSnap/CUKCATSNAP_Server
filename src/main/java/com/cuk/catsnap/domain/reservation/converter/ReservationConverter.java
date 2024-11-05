package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.member.converter.MemberConverter;
import com.cuk.catsnap.domain.member.dto.MemberResponse;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationConverter {

    private final MemberConverter memberConverter;
    /*
    * ReservationTimeFormat은 Nosql의 도큐먼트 입니다.
     */
    public ReservationTimeFormat toReservationTimeFormat(ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat, Long photographerId) {
        return ReservationTimeFormat.builder()
                .photographerId(photographerId)
                .formatName(photographerReservationTimeFormat.getFormatName())
                .startTimeList(photographerReservationTimeFormat.getStartTimeList())
                .build();
    }

    public ReservationResponse.PhotographerReservationTimeFormatList toPhotographerReservationTimeFormatList(List<ReservationTimeFormat> reservationTimeFormatList) {
                List<ReservationResponse.PhotographerReservationTimeFormat> responsePhotographerReservationTimeFormatList = reservationTimeFormatList.stream()
                .map(reservationTimeFormat -> ReservationResponse.PhotographerReservationTimeFormat.builder()
                        .reservationTimeFormatId(reservationTimeFormat.getId())
                        .formatName(reservationTimeFormat.getFormatName())
                        .startTimeList(reservationTimeFormat.getStartTimeList())
                        .build()
                )
                .toList();

        return ReservationResponse.PhotographerReservationTimeFormatList.builder()
                .reservationTimeFormatList(responsePhotographerReservationTimeFormatList)
                .build();
    }

    public Program toProgram(ReservationRequest.PhotographerProgram photographerProgram, Photographer photographer) {
        return Program.builder()
                .photographer(photographer)
                .title(photographerProgram.getTitle())
                .content(photographerProgram.getContent())
                .price(photographerProgram.getPrice())
                .durationMinutes(photographerProgram.getDurationMinutes())
                .deleted(false)
                .build();
    }

    public ReservationResponse.PhotographerProgramList toPhotographerProgramList(List<Program> programList) {
        List<ReservationResponse.PhotographerProgram> responsePhotographerProgramList = programList.stream()
                .map(program -> ReservationResponse.PhotographerProgram.builder()
                        .programId(program.getId())
                        .title(program.getTitle())
                        .content(program.getContent())
                        .price(program.getPrice())
                        .durationMinutes(program.getDurationMinutes())
                        .build()
                )
                .toList();

        return ReservationResponse.PhotographerProgramList.builder()
                .photographerProgramList(responsePhotographerProgramList)
                .build();
    }
    public ReservationResponse.MonthReservationCheckList toMonthReservationCheckList(List<Reservation> programList) {
        List<ReservationResponse.MonthReservationCheck> monthReservationCheckList = programList.stream()
                .map(program -> ReservationResponse.MonthReservationCheck.builder()
                        .reservationDate(program.getStartTime().toLocalDate())
                        .reservationState(program.getReservationState())
                        .build()
                )
                .toList();
        return ReservationResponse.MonthReservationCheckList.builder()
                .monthReservationCheckList(monthReservationCheckList)
                .build();
    }

    public ReservationResponse.PhotographerReservationInformation toPhotographerReservationInformation(Reservation reservation) {
        MemberResponse.MemberTinyInformation memberTinyInformation = memberConverter.toMemberTinyInformation(reservation.getMember());
        return ReservationResponse.PhotographerReservationInformation.builder()
                .reservationId(reservation.getId())
                .memberTinyInformation(memberTinyInformation)
                .location(toLocation(reservation))
                .startTime(reservation.getStartTime())
                .reservedProgram(toReservedProgram(reservation.getProgram()))
                .state(reservation.getReservationState())
                .build();
    }

    public ReservationResponse.ReservationBookResult toReservationBookResult(Reservation reservation) {
        return ReservationResponse.ReservationBookResult.builder()
                .reservationId(reservation.getId())
                .reservationState(reservation.getReservationState())
                .build();
    }

    public ReservationResponse.PhotographerAvailableReservationTimeList toPhotographerAvailableReservationTimeList(List<LocalTime> photographerStartTimeList, List<Reservation> reservationList){
        List<ReservationResponse.PhotographerAvailableReservationTime> photographerAvailableReservationTimeList = new ArrayList<>();
        /*
        * photographerStartTimeList는 작가가 설정한 예약 가능한 시간 목록입니다.
        * ReservationList는 현재까지 작가에게 예약된 예약 목록입니다.
        * 현재 예약 가능한 시간대를 조회하기 위해 예약된 시간대를 isAvailableReservation을 false로 설정하여 반환합니다.
         */
        for(LocalTime startTime : photographerStartTimeList){
            boolean isAvailableReservation = true;
            LocalDateTime startDateTime  = LocalDateTime.now().toLocalDate().atTime(startTime);
            for(Reservation reservation : reservationList){
                LocalDateTime reservationStartTime = reservation.getStartTime();
                LocalDateTime reservationEndTime = reservation.getEndTime();
                if(reservationStartTime.isAfter(startDateTime) || reservationStartTime.isEqual(startDateTime)
                    && reservationEndTime.isBefore(startDateTime) || reservationEndTime.isEqual(startDateTime)){
                    isAvailableReservation = false;
                    break;
                }
            }
            photographerAvailableReservationTimeList.add(ReservationResponse.PhotographerAvailableReservationTime.builder()
                    .startTime(startTime)
                    .isAvailableReservation(isAvailableReservation)
                    .build());
        }

        return ReservationResponse.PhotographerAvailableReservationTimeList.builder()
                .photographerAvailableReservationTimeList(photographerAvailableReservationTimeList)
                .build();
    }

    public ReservationResponse.Location toLocation(Reservation reservation) {
        return ReservationResponse.Location.builder()
                .latitude(reservation.getLocation().getY())
                .longitude(reservation.getLocation().getX())
                .locationName(reservation.getLocationName())
                .build();
    }

    public ReservationResponse.ReservedProgram toReservedProgram(Program program) {
        return ReservationResponse.ReservedProgram.builder()
                .title(program.getTitle())
                .content(program.getContent())
                .durationMinutes(program.getDurationMinutes())
                .price(program.getPrice())
                .build();
    }
}
