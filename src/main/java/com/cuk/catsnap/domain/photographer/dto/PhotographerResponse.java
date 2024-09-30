package com.cuk.catsnap.domain.photographer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class PhotographerResponse {

    @Getter
    @Builder
    @Schema(description = "작가의 Id와 닉네임", nullable = false)
    public static class PhotographerTinyInformation {
        private Long photographerId;
        private String nickname;
        private String profilePhotoUrl;
    }
}
