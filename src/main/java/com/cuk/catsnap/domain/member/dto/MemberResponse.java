package com.cuk.catsnap.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class MemberResponse {

    @Getter
    @Builder
    @Schema(description = "모델의 Id와 닉네임", nullable = false)
    public static class MemberTinyInformation {

        private Long memberId;
        private String nickname;
        private String profilePhotoUrl;
    }
}
