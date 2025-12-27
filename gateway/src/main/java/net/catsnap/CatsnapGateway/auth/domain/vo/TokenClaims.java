package net.catsnap.CatsnapGateway.auth.domain.vo;

import net.catsnap.shared.auth.CatsnapAuthority;

/**
 * JWT 토큰의 클레임 정보를 나타내는 Value Object입니다.
 * 토큰에서 추출한 사용자 정보를 도메인 개념으로 캡슐화합니다.
 */
public record TokenClaims(
    Long userId,
    CatsnapAuthority authority
) {

    private static final Long ANONYMOUS_USER_ID = -1L;

    /**
     * TokenClaims 생성자. 클레임 정보의 유효성을 검증합니다.
     *
     * @param userId 사용자 ID
     * @param authority 권한 enum
     * @throws IllegalArgumentException userId가 null이거나, authority가 null인 경우
     */
    public TokenClaims {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (authority == null) {
            throw new IllegalArgumentException("Authority cannot be null");
        }
    }

    /**
     * 익명 사용자의 클레임 정보를 생성합니다.
     *
     * @return 익명 사용자 TokenClaims
     */
    public static TokenClaims anonymous() {
        return new TokenClaims(ANONYMOUS_USER_ID, CatsnapAuthority.ANONYMOUS);
    }

    /**
     * 현재 클레임이 익명 사용자인지 확인합니다.
     *
     * @return 익명 사용자이면 true, 아니면 false
     */
    public boolean isAnonymous() {
        return authority == CatsnapAuthority.ANONYMOUS;
    }

    /**
     * 현재 클레임이 인증된 사용자인지 확인합니다.
     *
     * @return 인증된 사용자이면 true, 아니면 false
     */
    public boolean isAuthenticated() {
        return !isAnonymous();
    }
}
