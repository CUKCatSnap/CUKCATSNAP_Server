package com.cuk.catsnap.domain.photographer.dto.response;

import com.cuk.catsnap.domain.photographer.entity.Photographer;

public record PhotographerTinyInformationResponse(
    Long photographerId,
    String nickname,
    String profilePhotoUrl
) {

    public static PhotographerTinyInformationResponse from(Photographer photographer) {
        return new PhotographerTinyInformationResponse(photographer.getId(),
            photographer.getNickname(),
            photographer.getProfilePhotoUrl());
    }
}
