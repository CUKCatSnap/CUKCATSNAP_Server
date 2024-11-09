package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record MonthReservationCheckResponse(
    @Schema(description = "질문한 달의 예약이 있는 날", example = "yyyy-MM-dd", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate reservationDate,
    @Schema(description = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED")
    ReservationState reservationState
) {

    public static MonthReservationCheckResponse from(Reservation reservation) {
        return new MonthReservationCheckResponse(
            reservation.getStartTime().toLocalDate(),
            reservation.getReservationState()
        );
    }
}
