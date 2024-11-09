package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.global.jsonformat.deserialize.HoursMinutesListSerializer;
import com.cuk.catsnap.global.jsonformat.serializer.HoursMinutesListDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationResponse {

    @Getter
    @Builder
    @Schema(description = "예약의 위치를 나타내는 필드", nullable = false)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location {

        private Double latitude;
        private Double longitude;
        private String locationName;
    }

    @Getter
    @Builder
    public static class PhotographerReservationTimeFormatList {

        private List<PhotographerReservationTimeFormat> reservationTimeFormatList;
    }

    @Getter
    @Builder
    public static class PhotographerReservationTimeFormat {

        private String reservationTimeFormatId;
        private String formatName;
        @Schema(description = "예약의 시작 시간", example = "HH:mm", type = "string")
        @JsonSerialize(using = HoursMinutesListSerializer.class)
        @JsonDeserialize(using = HoursMinutesListDeserializer.class)
        private List<LocalTime> startTimeList;
    }
}
