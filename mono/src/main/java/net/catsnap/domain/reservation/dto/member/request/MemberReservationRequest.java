package net.catsnap.domain.reservation.dto.member.request;

import net.catsnap.domain.reservation.dto.ReservationLocation;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record MemberReservationRequest(
    Long photographerId,
    ReservationLocation reservationLocation,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startTime,
    Long programId
) {

}
