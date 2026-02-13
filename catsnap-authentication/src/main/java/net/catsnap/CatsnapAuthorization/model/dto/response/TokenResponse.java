package net.catsnap.CatsnapAuthorization.model.dto.response;

/**
 * 토큰 응답 DTO
 *
 * @param accessToken  액세스 토큰
 * @param refreshToken 리프레시 토큰
 */
public record TokenResponse(
    String accessToken,
    String refreshToken
) {
}