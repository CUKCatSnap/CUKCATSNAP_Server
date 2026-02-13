package net.catsnap.CatsnapGateway.auth.domain.vo;

/**
 * JWT 토큰을 나타내는 Value Object입니다. 토큰 문자열을 캡슐화하여 도메인 개념으로 표현합니다.
 */
public record Token(
    String value
) {

    /**
     * Token 생성자. 토큰 문자열의 유효성을 검증합니다.
     *
     * @param value 토큰 문자열
     * @throws IllegalArgumentException 토큰이 null이거나 빈 문자열인 경우
     */
    public Token {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Token value cannot be null or empty");
        }
    }
}
