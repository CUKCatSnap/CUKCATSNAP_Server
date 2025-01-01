package net.catsnap.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record TermsAgreementRequest(
    @Schema(description = "동의한 약관의 아이디")
    String termsId,
    @Schema(description = "동의 여부")
    Boolean isAgree
) {

}
