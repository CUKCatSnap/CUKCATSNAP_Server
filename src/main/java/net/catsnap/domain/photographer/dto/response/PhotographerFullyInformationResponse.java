package net.catsnap.domain.photographer.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "작가의 Id와 닉네임", nullable = false)
public record PhotographerFullyInformationResponse(
    Long photographerId,
    String nickname,
    String profilePhotoUrl,
    Double photographerRating,
    Integer recentReservation
) {

}
