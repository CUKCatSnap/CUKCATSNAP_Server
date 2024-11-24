package net.catsnap.domain.reservation.dto.photographer.response;

import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.global.jsonformat.deserialize.HoursMinutesListSerializer;
import net.catsnap.global.jsonformat.serializer.HoursMinutesListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;

public record ReservationTimeFormatResponse(
    String reservationTimeFormatId,
    String formatName,
    @Schema(description = "예약의 시작 시간", example = "HH:mm", type = "string")
    @JsonSerialize(using = HoursMinutesListSerializer.class)
    @JsonDeserialize(using = HoursMinutesListDeserializer.class)
    List<LocalTime> startTimeList
) {

    public static ReservationTimeFormatResponse from(ReservationTimeFormat reservationTimeFormat) {
        return new ReservationTimeFormatResponse(
            reservationTimeFormat.getId(),
            reservationTimeFormat.getFormatName(),
            reservationTimeFormat.getStartTimeList()
        );
    }
}
