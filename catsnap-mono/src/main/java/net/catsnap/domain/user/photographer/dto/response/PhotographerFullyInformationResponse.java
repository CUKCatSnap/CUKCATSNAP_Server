package net.catsnap.domain.user.photographer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import net.catsnap.domain.user.photographer.entity.Photographer;

@Schema(description = "작가의 Id와 닉네임", nullable = false)
public record PhotographerFullyInformationResponse(
    Long photographerId,
    String nickname,
    String profilePhotoUrl,
    Double photographerRating,
    Long recentReservation
) {

    public static PhotographerFullyInformationResponse from(
        Photographer photographer,
        Double photographerRating,
        Long recentReservation
    ) {
        return new PhotographerFullyInformationResponse(
            photographer.getId(),
            photographer.getNickname(),
            photographer.getProfilePhotoUrl(),
            photographerRating,
            recentReservation
        );
    }
}
