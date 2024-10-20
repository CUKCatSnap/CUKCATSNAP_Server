package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.cuk.catsnap.global.jsonformat.deserialize.HoursMinutesListSerializer;
import com.cuk.catsnap.global.jsonformat.serializer.HoursMinutesListDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class ReservationRequest {

    @Getter
    @NoArgsConstructor
    public static class ReservationBook {
        private Long photographerId;
        private ReservationResponse.Location location;
        @Schema(description = "예약시 시작할 시간을 입력. 만약 작가가 설정한 시작 시간과 일치하지 않으면 오류 발생", example = "yyyy-MM-dd HH:mm:ss", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime startTime;
        private String programId;
        private ReservationState reservationState;
    }

    /*
    * 특정 형식은 여러개의 시작 시간을 가지고 있음.
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotographerReservationTimeFormat {
        @Schema(description = "예약 시간 형식의 이름", example = "주말용 형식", type = "string")
        private String formatName;

        @Schema(description = "예약 시작 시간", example = "HH:mm", type = "string")
        @JsonSerialize(using = HoursMinutesListSerializer.class)
        @JsonDeserialize(using = HoursMinutesListDeserializer.class)
        private List<LocalTime> startTimeList;
    }

    @Getter
    @NoArgsConstructor
    public static class PhotographerProgram {
        private String title;
        private String content;
        private Long price;
        private Long duration;
    }
}
