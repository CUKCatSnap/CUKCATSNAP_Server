package net.catsnap.domain.reservation.dto.member.response;

import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationBookResultResponse(
    Long reservationId,
    @Schema(description = "예약의 상태를 의미합니다. 작가의 설정에 따라 바로 완료 될 수도 있고, 완료 대기일 수도 있습니다. (PENDING, APPROVED)")
    ReservationState reservationState
) {

    public static ReservationBookResultResponse from(Reservation reservation) {
        return new ReservationBookResultResponse(reservation.getId(),
            reservation.getReservationState());
    }
}
