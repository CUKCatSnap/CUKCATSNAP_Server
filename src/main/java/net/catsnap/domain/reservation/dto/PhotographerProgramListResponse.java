package net.catsnap.domain.reservation.dto;

import java.util.List;

public record PhotographerProgramListResponse(
    List<PhotographerProgramResponse> photographerProgramList
) {

    public static PhotographerProgramListResponse from(
        List<PhotographerProgramResponse> photographerProgramList) {
        return new PhotographerProgramListResponse(
            photographerProgramList);
    }
}
