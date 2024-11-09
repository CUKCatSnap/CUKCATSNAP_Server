package com.cuk.catsnap.domain.reservation.dto.photographer.response;

import java.util.List;

public record ReservationTimeFormatListResponse(
    List<ReservationTimeFormatResponse> reservationTimeFormatList
) {

    public static ReservationTimeFormatListResponse from(
        List<ReservationTimeFormatResponse> reservationTimeFormatList) {
        return new ReservationTimeFormatListResponse(reservationTimeFormatList);
    }
}
