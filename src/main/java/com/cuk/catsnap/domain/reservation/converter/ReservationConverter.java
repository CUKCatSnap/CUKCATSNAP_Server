package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.member.dto.MemberResponse;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationConverter {

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

    public ReservationResponse.PhotographerReservationInformation toPhotographerReservationInformation(Reservation reservation, MemberResponse.MemberTinyInformation memberTinyInformation) {
        return ReservationResponse.PhotographerReservationInformation.builder()
                .reservationId(reservation.getId())
                .memberTinyInformation(memberTinyInformation)
                .location(ReservationResponse.Location.builder()
                        .lat(reservation.getLocation().getY())
                        .lng(reservation.getLocation().getX())
                        .locationName(reservation.getLocationName())
                        .build())
                .time(ReservationResponse.Time.builder()
                        .startTime(reservation.getStartTime())
                        .endTime(reservation.getEndTime())
                        .build())
                .reservedProgram(ReservationResponse.ReservedProgram.builder()
                        .title(reservation.getProgram().getTitle())
                        .content(reservation.getProgram().getContent())
                        .price(reservation.getProgram().getPrice())
                        .build())
                .state(reservation.getReservationState())
                .build();
    }
}
