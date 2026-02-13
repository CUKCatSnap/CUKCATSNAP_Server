package net.catsnap.CatsnapReservation.reservation.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 예약 생성 요청 DTO
 */
public record ReservationCreateRequest(
    @NotNull(message = "프로그램 ID는 필수입니다")
    Long programId,

    @NotNull(message = "예약 날짜는 필수입니다")
    LocalDate reservationDate,

    @NotNull(message = "시작 시간은 필수입니다")
    LocalTime startTime
) {

}