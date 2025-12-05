package net.catsnap.global.security.dto;

public record AuthTokenDTO(String accessToken, String refreshToken) {

    public static AuthTokenDTO of(String accessToken, String refreshToken) {
        return new AuthTokenDTO(accessToken, refreshToken);
    }
}
