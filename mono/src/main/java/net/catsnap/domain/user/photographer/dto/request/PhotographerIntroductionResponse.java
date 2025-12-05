package net.catsnap.domain.user.photographer.dto.request;

import net.catsnap.domain.user.photographer.entity.PhotographerIntroduction;

public record PhotographerIntroductionResponse(
    String introduction
) {

    public static PhotographerIntroductionResponse from(
        PhotographerIntroduction photographerIntroduction) {
        return new PhotographerIntroductionResponse(
            photographerIntroduction.getContent()
        );
    }
}
