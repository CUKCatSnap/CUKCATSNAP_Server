package net.catsnap.CatsnapAuthorization.photographer.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 작가 로그인 요청 DTO
 *
 * @param identifier 사용자 식별자
 * @param password   비밀번호
 */
public record PhotographerLoginRequest(
    @NotBlank(message = "식별자는 필수입니다")
    String identifier,

    @NotBlank(message = "비밀번호는 필수입니다")
    String password
) {
}