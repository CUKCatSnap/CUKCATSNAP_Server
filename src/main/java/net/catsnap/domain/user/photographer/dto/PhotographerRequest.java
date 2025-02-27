package net.catsnap.domain.user.photographer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PhotographerRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "작가 자신의 예약 세팅 정보")
    public static class PhotographerSetting {

        @Schema(description = "작가가 예약을 자동으로 수락할지 여부. true면 자동 수락, false면 수락 대기")
        private Boolean autoReservationAccept;
        @Schema(description = "작가가 오버부킹을 허용할지 여부. true면 허용, false면 허용하지 않음")
        private Boolean enableOverBooking;
        @Schema(description = "작가가 예약을 현재를 기준으로 며칠 후 까지 예약을 받을지")
        private Long preReservationDays;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "예약전  공지 사항")
    public static class PhotographerReservationNotice {

        @Schema(description = "예약전  공지 사항")
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "작가의 예약 가능 지역")
    public static class PhotographerReservationLocation {

        @Schema(description = "작가의 예약 가능 지역")
        private String content;
    }
}
