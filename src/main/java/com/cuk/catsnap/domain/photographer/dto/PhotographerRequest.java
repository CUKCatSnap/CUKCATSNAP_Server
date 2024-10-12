package com.cuk.catsnap.domain.photographer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class PhotographerRequest {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotographerSignUp {

        private String identifier;
        private String password;
        private String nickname;

        @Schema(description = "생년 월일", example = "yyyy-MM-dd", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthday;

        @Schema(description = "휴대전화 번호", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(description = "약관 동의 여부")
        private List<PhotographerRequest.TermsAgreement> termsAgreementList;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TermsAgreement{
        @Schema(description = "동의한 약관의 아이디")
        private String termsId;
        @Schema(description = "동의 여부")
        private Boolean isAgree;
    }
}
