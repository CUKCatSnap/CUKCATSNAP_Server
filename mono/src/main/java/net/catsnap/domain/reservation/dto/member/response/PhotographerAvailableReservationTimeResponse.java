package net.catsnap.domain.reservation.dto.member.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

public record PhotographerAvailableReservationTimeResponse(
    @Schema(description = "예약 가능한 시간", example = "HH:mm", type = "string")
    @JsonFormat(pattern = "HH:mm")
    LocalTime startTime,
    @Schema(description = "true이면 해당 시간에 예약이 가능함")
    Boolean isAvailableReservation
) {

    public static PhotographerAvailableReservationTimeResponse of(LocalTime startTime,
        Boolean isAvailableReservation) {
        return new PhotographerAvailableReservationTimeResponse(startTime, isAvailableReservation);
    }
}
