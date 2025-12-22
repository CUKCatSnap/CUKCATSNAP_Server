package net.catsnap.CatsnapAuthorization.session.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * 로그인 세션 애그리거트 루트
 *
 * <p>액세스 토큰과 리프레시 토큰의 생명주기를 관리하는 애그리거트입니다.
 * 로그인 시 생성되며, 로그아웃 시 함께 무효화됩니다.</p>
 */
@RedisHash(value = "loginSession")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSession {

    // 세션 TTL
    private static final long LOGIN_SESSION_DURATION_SECONDS = 30 * 24 * 60 * 60L;  // 30일

    @Id
    private String sessionKey;

    private Long userId;

    private CatsnapAuthority authority;

    private LocalDateTime loginAt;

    private LocalDateTime lastAccessedAt;

    @TimeToLive  // Redis TTL (초 단위)
    private final Long sessionLiveDuration = LOGIN_SESSION_DURATION_SECONDS;

    /**
     * 로그인 시 세션을 생성하는 정적 팩토리 메서드
     *
     * @param userId    사용자 ID
     * @param authority 권한
     * @return 생성된 LoginSession
     */
    public static LoginSession create(Long userId, CatsnapAuthority authority) {
        String sessionKey = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        return new LoginSession(sessionKey, userId, authority, now, now);
    }

    private LoginSession(String sessionKey, Long userId, CatsnapAuthority authority,
        LocalDateTime loginAt, LocalDateTime lastAccessedAt) {
        this.sessionKey = sessionKey;
        this.userId = userId;
        this.authority = authority;
        this.loginAt = loginAt;
        this.lastAccessedAt = lastAccessedAt;
    }

    /**
     * 액세스 토큰을 생성합니다.
     *
     * <p>로그인 세션을 통해 액세스 토큰을 발급하며, 마지막 접근 시간을 갱신합니다.</p>
     *
     * @param accessTokenManager 액세스 토큰 발급자
     * @return 생성된 액세스 토큰
     */
    public String generateAccessToken(AccessTokenManager accessTokenManager) {
        updateLastAccessedAt();
        return accessTokenManager.issue(userId, authority);
    }

    /**
     * 마지막 접근 시간을 현재 시간으로 갱신합니다.
     */
    private void updateLastAccessedAt() {
        this.lastAccessedAt = LocalDateTime.now();
    }
}