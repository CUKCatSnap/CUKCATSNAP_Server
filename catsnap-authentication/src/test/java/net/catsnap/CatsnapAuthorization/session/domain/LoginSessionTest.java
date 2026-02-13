package net.catsnap.CatsnapAuthorization.session.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LoginSessionTest {

    @Mock
    private AccessTokenManager mockTokenManager;

    private static final long EXPECTED_TTL_SECONDS = 30 * 24 * 60 * 60L; // 30일

    @Test
    void create_메서드로_LoginSession을_생성한다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        LoginSession loginSession = LoginSession.create(userId, authority);

        // then
        assertThat(loginSession).isNotNull();
        assertThat(loginSession.getSessionKey()).isNotNull();
        assertThat(loginSession.getSessionKey()).isNotEmpty();
        assertThat(loginSession.getUserId()).isEqualTo(userId);
        assertThat(loginSession.getAuthority()).isEqualTo(authority);
    }

    @Test
    void create_메서드로_생성된_세션의_sessionKey는_UUID_형식이다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        LoginSession loginSession = LoginSession.create(userId, authority);

        // then
        // UUID 형식: 8-4-4-4-12
        String sessionKey = loginSession.getSessionKey();
        assertThat(sessionKey).matches(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
    }

    @Test
    void create_메서드로_생성된_세션의_createdAt과_lastAccessedAt이_설정된다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;
        LocalDateTime beforeCreate = LocalDateTime.now().minusSeconds(1);

        // when
        LoginSession loginSession = LoginSession.create(userId, authority);

        // then
        LocalDateTime afterCreate = LocalDateTime.now().plusSeconds(1);

        assertThat(loginSession.getLoginAt()).isAfter(beforeCreate);
        assertThat(loginSession.getLoginAt()).isBefore(afterCreate);
        assertThat(loginSession.getLastAccessedAt()).isAfter(beforeCreate);
        assertThat(loginSession.getLastAccessedAt()).isBefore(afterCreate);

        // loginAt과 lastAccessedAt은 같은 시간이어야 함
        assertThat(loginSession.getLoginAt()).isEqualTo(loginSession.getLastAccessedAt());
    }

    @Test
    void create_메서드로_생성된_세션의_TTL은_30일이다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        LoginSession loginSession = LoginSession.create(userId, authority);

        // then
        assertThat(loginSession.getSessionLiveDuration()).isEqualTo(EXPECTED_TTL_SECONDS);
    }

    @Test
    void 다른_userId로_생성하면_다른_sessionKey가_발급된다() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        LoginSession session1 = LoginSession.create(userId1, authority);
        LoginSession session2 = LoginSession.create(userId2, authority);

        // then
        assertThat(session1.getSessionKey()).isNotEqualTo(session2.getSessionKey());
    }

    @Test
    void generateAccessToken_메서드는_AccessTokenManager를_통해_토큰을_생성한다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;
        LoginSession loginSession = LoginSession.create(userId, authority);

        String expectedToken = "mock.jwt.token";
        when(mockTokenManager.issue(userId, authority)).thenReturn(expectedToken);

        // when
        String actualToken = loginSession.generateAccessToken(mockTokenManager);

        // then
        assertThat(actualToken).isEqualTo(expectedToken);
        verify(mockTokenManager).issue(eq(userId), eq(authority));
    }

    @Test
    void generateAccessToken_메서드는_lastAccessedAt을_갱신한다() throws InterruptedException {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;
        LoginSession loginSession = LoginSession.create(userId, authority);

        LocalDateTime initialLastAccessedAt = loginSession.getLastAccessedAt();

        // 시간 차이를 만들기 위해 약간 대기
        Thread.sleep(10);

        when(mockTokenManager.issue(any(), any())).thenReturn("token");

        // when
        loginSession.generateAccessToken(mockTokenManager);

        // then
        assertThat(loginSession.getLastAccessedAt()).isAfter(initialLastAccessedAt);
    }
}