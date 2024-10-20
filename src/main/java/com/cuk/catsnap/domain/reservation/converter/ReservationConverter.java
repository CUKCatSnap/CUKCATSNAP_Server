package com.cuk.catsnap.domain.reservation.converter;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import com.cuk.catsnap.domain.reservation.dto.ReservationRequest;
import org.springframework.stereotype.Component;

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
}
