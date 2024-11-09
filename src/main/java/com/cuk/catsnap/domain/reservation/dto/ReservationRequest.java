package com.cuk.catsnap.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationRequest {

    @Getter
    @NoArgsConstructor
    public static class PhotographerProgram {

        private String title;
        private String content;
        private Long price;
        @Schema(description = "프로그램의 소요 시간(분)", example = "60", type = "integer")
        private Long durationMinutes;
    }
}
