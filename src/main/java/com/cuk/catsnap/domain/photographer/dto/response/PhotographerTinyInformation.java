package com.cuk.catsnap.domain.photographer.dto.response;

import com.cuk.catsnap.domain.photographer.entity.Photographer;

public record PhotographerTinyInformation(
    Long photographerId,
    String nickname,
    String profilePhotoUrl
) {

    public static PhotographerTinyInformation from(Photographer photographer) {
        return new PhotographerTinyInformation(photographer.getId(), photographer.getNickname(),
            photographer.getProfilePhotoUrl());
    }
}
