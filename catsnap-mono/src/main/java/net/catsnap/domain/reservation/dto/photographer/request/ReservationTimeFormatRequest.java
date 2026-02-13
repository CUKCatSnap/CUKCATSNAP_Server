package net.catsnap.domain.reservation.dto.photographer.request;

import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.global.jsonformat.deserialize.HoursMinutesListSerializer;
import net.catsnap.global.jsonformat.serializer.HoursMinutesListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;

public record ReservationTimeFormatRequest(
    @Schema(description = "예약 시간 형식의 이름", example = "주말용 형식", type = "string")
    String formatName,
    @Schema(description = "예약 시작 시간", example = "HH:mm", type = "string")
    @JsonSerialize(using = HoursMinutesListSerializer.class)
    @JsonDeserialize(using = HoursMinutesListDeserializer.class)
    List<LocalTime> startTimeList
) {

    public ReservationTimeFormat toEntity(Long photographerId) {
        return ReservationTimeFormat.builder()
            .photographerId(photographerId)
            .formatName(formatName)
            .startTimeList(startTimeList)
            .build();
    }

    public ReservationTimeFormat toEntity(String id, Long photographerId) {
        return ReservationTimeFormat.builder()
            .id(id)
            .photographerId(photographerId)
            .formatName(formatName)
            .startTimeList(startTimeList)
            .build();
    }
}
