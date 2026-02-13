package net.catsnap.CatsnapAuthorization.session.application.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * 토큰 갱신 요청 DTO
 *
 * @param refreshToken 리프레시 토큰 (세션 키)
 */
public record TokenRefreshRequest(
    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    String refreshToken
) {

}