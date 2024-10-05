package com.cuk.catsnap.domain.reservation.dto;

import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
