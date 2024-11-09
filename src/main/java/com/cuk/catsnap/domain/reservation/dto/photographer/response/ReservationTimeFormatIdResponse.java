package com.cuk.catsnap.domain.reservation.dto.photographer.response;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;

public record ReservationTimeFormatIdResponse(
    String reservationTimeFormatId
) {

    public static ReservationTimeFormatIdResponse from(
        ReservationTimeFormat reservationTimeFormat) {
        return new ReservationTimeFormatIdResponse(reservationTimeFormat.getId());
    }
}
