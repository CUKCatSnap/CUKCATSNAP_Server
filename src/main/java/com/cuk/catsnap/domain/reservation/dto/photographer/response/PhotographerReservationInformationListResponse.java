package com.cuk.catsnap.domain.reservation.dto.photographer.response;

import java.util.List;

public record PhotographerReservationInformationListResponse(
    List<PhotographerReservationInformationResponse> photographerReservationInformationResponseList
) {

    public static PhotographerReservationInformationListResponse from(
        List<PhotographerReservationInformationResponse> photographerReservationInformationResponseList) {
        return new PhotographerReservationInformationListResponse(
            photographerReservationInformationResponseList);
    }
}
