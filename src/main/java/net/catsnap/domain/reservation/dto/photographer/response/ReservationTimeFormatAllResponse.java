package net.catsnap.domain.reservation.dto.photographer.response;

import net.catsnap.domain.reservation.entity.Weekday;

public record ReservationTimeFormatAllResponse(
    Weekday weekday,
    ReservationTimeFormatResponse reservationTimeFormat
) {

}
