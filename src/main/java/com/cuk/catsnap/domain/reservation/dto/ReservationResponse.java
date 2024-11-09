package com.cuk.catsnap.domain.reservation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
}
