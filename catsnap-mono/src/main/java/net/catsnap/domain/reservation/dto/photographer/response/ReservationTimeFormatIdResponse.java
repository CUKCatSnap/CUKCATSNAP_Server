package net.catsnap.domain.reservation.dto.photographer.response;

import net.catsnap.domain.reservation.document.ReservationTimeFormat;

public record ReservationTimeFormatIdResponse(
    String reservationTimeFormatId
) {

    public static ReservationTimeFormatIdResponse from(
        ReservationTimeFormat reservationTimeFormat) {
        return new ReservationTimeFormatIdResponse(reservationTimeFormat.getId());
    }
}
