package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Getter
    @NoArgsConstructor
    public static class PhotographerReservationTimeFormat {
        @Schema(description = "예약 시간 형식의 이름", example = "주말용 형식", type = "string")
        private String formatName;
        private List<startTimeEndTime> startTimeEndTimeList;
    }

    /*
    * 작가가 예약 시간 형식을 설정할 때 사용하는 DTO
     */
    @Getter
    @NoArgsConstructor
    public static class startTimeEndTime {
        @Schema(description = "시작 시간", example = "HH:mm", type = "string")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime startTime;
        @Schema(description = "종료 시간", example = "HH:mm", type = "string")
        @JsonFormat(pattern = "HH:mm")
        private LocalTime endTime;
    }

    @Getter
    @NoArgsConstructor
    public static class PhotographerReservationWeekdayTimeFormat {
        private List<PhotographerReservationTimeFormat> photographerReservationTimeFormatList;
    }

    @Getter
    @NoArgsConstructor
    public static class PhotographerReservationTimeFormatList {
        private List<PhotographerReservationTimeFormat> photographerReservationTimeFormatList;
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
