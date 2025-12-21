package net.catsnap.CatsnapAuthorization.session.domain;

import net.catsnap.shared.auth.CatsnapAuthority;

/**
 * 액세스 토큰 발급 인터페이스 (Domain Layer)
 *
 * <p>DDD 원칙에 따라 인터페이스는 도메인 계층에 위치하며,
 * 구현은 인프라 계층에서 제공합니다.</p>
 */
public interface AccessTokenManager {

    /**
     * 액세스 토큰을 발급합니다.
     *
     * @param userId    사용자 ID
     * @param authority 권한
     * @return 발급된 액세스 토큰
     */
    String issue(Long userId, CatsnapAuthority authority);
}