package net.catsnap.CatsnapReservation.reservation.application.dto.response;

import java.time.LocalDateTime;

/**
 * 예약 생성 응답 DTO
 */
public record ReservationCreateResponse(
    String reservationNumber,
    Long amount,
    LocalDateTime holdExpiresAt
) {

}