package net.catsnap.domain.reservation.dto.member.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import net.catsnap.domain.user.photographer.dto.response.PhotographerTinyInformationResponse;
import net.catsnap.domain.reservation.dto.ReservationLocation;
import net.catsnap.domain.reservation.dto.ReservedProgramResponse;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationState;

public record MemberReservationInformationResponse(
    Long reservationId,
    PhotographerTinyInformationResponse photographerTinyInformationResponse,
    ReservationLocation reservationLocation,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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
