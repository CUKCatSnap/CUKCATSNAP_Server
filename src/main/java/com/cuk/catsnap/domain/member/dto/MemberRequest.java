package com.cuk.catsnap.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberRequest {

    @Getter
    @NoArgsConstructor
    public static class MemberSignUp {

        private String identifier;
        private String password;
        private String name;
        private String nickname;

        @Schema(description = "생년 월일", example = "yyyy-MM-dd", type = "string")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime birth;

        @Schema(description = "휴대전화 번호", example = "010-1234-5678")
        private String phoneNumber;

        @Schema(description = "서비스 이용 약관 동의 여부", example = "true")
        private Boolean termsOfServiceAgreement;

        @Schema(description = "개인정보 수집 및 이용 동의", example = "true")
        private Boolean privatePolicyAgreement;

        @Schema(description = "본인 확인 및 인증", example = "true")
        private Boolean identityVerificationAgreement;

        @Schema(description = "마케팅 정보 수신 동의(선택)", example = "true")
        private Boolean MarketingAgreement;

        @Schema(description = "맞춤형 광고 제공을 위한 개인정보 활용 동의(선택)", example = "true")
        private Boolean PersonalInformationAgreement;
    }
}
