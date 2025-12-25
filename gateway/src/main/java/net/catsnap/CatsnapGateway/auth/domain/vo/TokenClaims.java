package net.catsnap.CatsnapGateway.auth.domain.vo;

/**
 * JWT 토큰의 클레임 정보를 나타내는 Value Object입니다.
 * 토큰에서 추출한 사용자 정보를 도메인 개념으로 캡슐화합니다.
 */
public record TokenClaims(
    Long userId,
    String authority
) {

    /**
     * TokenClaims 생성자. 클레임 정보의 유효성을 검증합니다.
     *
     * @param userId 사용자 ID
     * @param authority 권한 문자열
     * @throws IllegalArgumentException userId가 null이거나, authority가 null이거나 빈 문자열인 경우
     */
    public TokenClaims {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (authority == null || authority.isBlank()) {
            throw new IllegalArgumentException("Authority cannot be null or empty");
        }
    }
}
