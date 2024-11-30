package net.catsnap.domain.auth.dto.photographer.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import net.catsnap.domain.auth.dto.TermsAgreementRequest;
import net.catsnap.domain.photographer.entity.Photographer;

public record PhotographerSignUpRequest(
    String identifier,
    String password,
    String nickname,
    @Schema(description = "생년 월일", example = "yyyy-MM-dd", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday,
    @Schema(description = "휴대전화 번호", example = "010-1234-5678")
    String phoneNumber,
    @Schema(description = "약관 동의 여부")
    List<TermsAgreementRequest> termsAgreementList
) {

    public Photographer toEntity(String hashedPassword) {
        return Photographer.builder()
            .identifier(identifier())
            .password(hashedPassword)
            .birthday(birthday())
            .nickname(nickname())
            .phoneNumber(phoneNumber())
            .build();
    }
}
