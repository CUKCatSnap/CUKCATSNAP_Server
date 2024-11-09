package com.cuk.catsnap.domain.reservation.dto.member.response;

import com.cuk.catsnap.domain.photographer.dto.response.PhotographerTinyInformationResponse;
import com.cuk.catsnap.domain.reservation.dto.ReservationLocation;
import com.cuk.catsnap.domain.reservation.dto.ReservedProgramResponse;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MemberReservationInformationResponse(
    Long reservationId,
    PhotographerTinyInformationResponse photographerTinyInformationResponse,
    ReservationLocation reservationLocation,
    LocalDateTime startTime,
    ReservedProgramResponse reservedProgramResponse,
    @Schema(description = "예약의 상태를 나타냅니다.", nullable = false, example = "PENDING, APPROVED, REJECTED, MEMBER_CANCELLED, PHOTOGRAPHY_CANCELLED 중 한개의 값")
    ReservationState state
) {

    public static MemberReservationInformationResponse from(Reservation reservation) {
        return new MemberReservationInformationResponse(
            reservation.getId(),
            PhotographerTinyInformationResponse.from(reservation.getPhotographer()),
            ReservationLocation.of(reservation.getLocation().getX(),
                reservation.getLocation().getY(), reservation.getLocationName()),
            reservation.getStartTime(),
            ReservedProgramResponse.from(reservation.getProgram()),
            reservation.getReservationState()
        );
    }
}
