package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
import com.cuk.catsnap.domain.reservation.entity.Program;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationConverter {

    /*
     * ReservationTimeFormat은 Nosql의 도큐먼트 입니다.
     */
    public ReservationTimeFormat toReservationTimeFormat(
        ReservationRequest.PhotographerReservationTimeFormat photographerReservationTimeFormat,
        Long photographerId) {
        return ReservationTimeFormat.builder()
            .photographerId(photographerId)
            .formatName(photographerReservationTimeFormat.getFormatName())
            .startTimeList(photographerReservationTimeFormat.getStartTimeList())
            .build();
    }

    public ReservationResponse.PhotographerReservationTimeFormatList toPhotographerReservationTimeFormatList(
        List<ReservationTimeFormat> reservationTimeFormatList) {
        List<ReservationResponse.PhotographerReservationTimeFormat> responsePhotographerReservationTimeFormatList = reservationTimeFormatList.stream()
            .map(
                reservationTimeFormat -> ReservationResponse.PhotographerReservationTimeFormat.builder()
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

    public Program toProgram(ReservationRequest.PhotographerProgram photographerProgram,
        Photographer photographer) {
        return Program.builder()
            .photographer(photographer)
            .title(photographerProgram.getTitle())
            .content(photographerProgram.getContent())
            .price(photographerProgram.getPrice())
            .durationMinutes(photographerProgram.getDurationMinutes())
            .deleted(false)
            .build();
    }
}
