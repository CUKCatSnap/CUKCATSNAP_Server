package net.catsnap.domain.user.photographer.dto.response;

import net.catsnap.domain.user.entity.UserTinyInformation;
import net.catsnap.domain.user.photographer.entity.Photographer;

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

    public static PhotographerTinyInformationResponse from(
        UserTinyInformation userTinyInformation) {
        return new PhotographerTinyInformationResponse(userTinyInformation.getId(),
            userTinyInformation.getNickname(),
            userTinyInformation.getProfilePhotoUrl());
    }
}
