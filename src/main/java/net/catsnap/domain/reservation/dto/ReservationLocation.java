package net.catsnap.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import net.catsnap.domain.reservation.entity.Reservation;

@Schema(description = "예약의 위치를 나타내는 필드", nullable = false)
public record ReservationLocation(
    Double latitude,
    Double longitude,
    String locationName
) {

    public static ReservationLocation of(Double latitude, Double longitude, String locationName) {
        return new ReservationLocation(latitude, longitude, locationName);
    }

    public static ReservationLocation of(Reservation reservation) {
        return new ReservationLocation(reservation.getLocation().getX(),
            reservation.getLocation().getY(), reservation.getLocationName());
    }
}
