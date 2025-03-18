package net.catsnap.domain.reservation.dto.photographer.response;

import java.util.List;

public record ReservationTimeFormatAllListResponse(
    List<ReservationTimeFormatAllResponse> reservationTimeFormatAllList
) {

    public static ReservationTimeFormatAllListResponse of(
        List<ReservationTimeFormatAllResponse> reservationTimeFormatAllList) {
        return new ReservationTimeFormatAllListResponse(
            reservationTimeFormatAllList
        );
    }
}
