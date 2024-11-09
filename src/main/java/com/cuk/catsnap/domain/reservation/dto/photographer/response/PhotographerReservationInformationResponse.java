package com.cuk.catsnap.domain.reservation.dto.photographer.response;

import com.cuk.catsnap.domain.member.dto.response.MemberTinyInformationResponse;
import com.cuk.catsnap.domain.reservation.dto.ReservationLocation;
import com.cuk.catsnap.domain.reservation.dto.ReservedProgramResponse;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record PhotographerReservationInformationResponse(
    Long reservationId,
    MemberTinyInformationResponse memberTinyInformationResponse,
    ReservationLocation reservationLocation,
    LocalDateTime startTime,
    ReservedProgramResponse reservedProgramResponse,
    @Schema(description = "예약의 상태를 타냅니다.", nullable = false, example = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED 중 한개의 값")
    ReservationState state
) {

    public static PhotographerReservationInformationResponse from(Reservation reservation) {
        return new PhotographerReservationInformationResponse(
            reservation.getId(),
            MemberTinyInformationResponse.from(reservation.getMember()),
            ReservationLocation.of(reservation.getLocation().getX(),
                reservation.getLocation().getY(), reservation.getLocationName()),
            reservation.getStartTime(),
            ReservedProgramResponse.from(reservation.getProgram()),
            reservation.getReservationState()
        );
    }
}
