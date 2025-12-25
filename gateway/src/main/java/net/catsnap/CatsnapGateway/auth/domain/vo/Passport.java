package net.catsnap.CatsnapGateway.auth.domain.vo;

import net.catsnap.shared.auth.CatsnapAuthority;

/**
 * 사용자 인증 정보를 나타내는 Value Object입니다.
 * 인증된 사용자의 고유 ID와 권한 정보를 불변 객체로 표현합니다.
 */
public record Passport(
    Long userId,
    CatsnapAuthority authority
) {

    private static final Long ANONYMOUS_USER_ID = -1L;

    /**
     * Passport 생성자. 인증 정보의 유효성을 검증합니다.
     *
     * @param userId 사용자 ID
     * @param authority 사용자 권한
     * @throws IllegalArgumentException userId 또는 authority가 null인 경우
     */
    public Passport {
        if (userId == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (authority == null) {
            throw new IllegalArgumentException("Authority cannot be null");
        }
    }

    /**
     * 익명 사용자의 Passport를 생성합니다.
     *
     * @return 익명 사용자 Passport
     */
    public static Passport createAnonymous() {
        return new Passport(ANONYMOUS_USER_ID, CatsnapAuthority.ANONYMOUS);
    }

    /**
     * 현재 사용자가 익명 사용자인지 확인합니다.
     *
     * @return 익명 사용자이면 true, 아니면 false
     */
    public boolean isAnonymous() {
        return authority == CatsnapAuthority.ANONYMOUS;
    }

    /**
     * 현재 사용자가 인증된 사용자인지 확인합니다.
     *
     * @return 인증된 사용자이면 true, 아니면 false
     */
    public boolean isAuthenticated() {
        return !isAnonymous();
    }
}
