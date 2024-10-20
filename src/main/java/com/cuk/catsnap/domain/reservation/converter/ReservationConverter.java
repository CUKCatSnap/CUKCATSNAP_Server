package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import com.cuk.catsnap.domain.reservation.dto.ReservationResponse;
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
                        .id(reservationTimeFormat.getId())
                        .formatName(reservationTimeFormat.getFormatName())
                        .startTimeList(reservationTimeFormat.getStartTimeList())
                        .build()
                )
                .toList();

        return ReservationResponse.PhotographerReservationTimeFormatList.builder()
                .reservationTimeFormatList(responsePhotographerReservationTimeFormatList)
                .build();
    }
}
