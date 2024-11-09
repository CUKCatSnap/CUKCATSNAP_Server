package com.cuk.catsnap.domain.reservation.dto.member.response;

import java.util.List;

public record PhotographerAvailableReservationTimeListResponse(
    List<PhotographerAvailableReservationTimeResponse> photographerAvailableReservationTimeList
) {

    public static PhotographerAvailableReservationTimeListResponse from(
        List<PhotographerAvailableReservationTimeResponse> photographerAvailableReservationTimeList) {
        return new PhotographerAvailableReservationTimeListResponse(
            photographerAvailableReservationTimeList);
    }
}
