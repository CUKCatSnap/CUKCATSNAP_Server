package com.cuk.catsnap.domain.reservation.dto.member.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PhotographerReservationGuidanceResponse(
    @Schema(description = "작가가 설정한 예약 가능한 장소입니다. 주소 형태가 아니라 문자열 형태입니다.")
    String photographerLocation,
    @Schema(description = "작가가 설정한 예약전 주의사항입니다.")
    String photographerNotification
) {

    public static PhotographerReservationGuidanceResponse of(String photographerLocation,
        String photographerNotification) {
        return new PhotographerReservationGuidanceResponse(photographerLocation,
            photographerNotification);
    }
}
