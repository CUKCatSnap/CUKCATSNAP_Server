package net.catsnap.CatsnapAuthorization.session.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtAccessTokenManagerTest {

    private JwtAccessTokenManager jwtAccessTokenManager;
    private SecretKey secretKey;
    private static final Long EXPIRATION_MINUTES = 30L;

    @BeforeEach
    void setUp() {
        // 테스트용 시크릿 키 생성
        secretKey = Jwts.SIG.HS256.key().build();
        jwtAccessTokenManager = new JwtAccessTokenManager(secretKey, EXPIRATION_MINUTES);
    }

    @Test
    void 액세스_토큰을_발급한다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        String token = jwtAccessTokenManager.issue(userId, authority);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    void 발급된_토큰의_헤더에_provider와_type이_올바르게_설정된다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        String token = jwtAccessTokenManager.issue(userId, authority);

        // then
        var jwt = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token);

        assertThat(jwt.getHeader().get("provider")).isEqualTo("catsnap");
        assertThat(jwt.getHeader().get("type")).isEqualTo("accessToken");
    }

    @Test
    void 발급된_토큰의_클레임에_userId와_authority가_올바르게_설정된다() {
        // given
        Long userId = 123L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;

        // when
        String token = jwtAccessTokenManager.issue(userId, authority);

        // then
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        assertThat(claims.get("id", Integer.class)).isEqualTo(userId.intValue());
        assertThat(claims.get("authority", String.class)).isEqualTo(authority.getAuthorityName());
    }

    @Test
    void 발급된_토큰의_만료_시간이_올바르게_설정된다() {
        // given
        Long userId = 1L;
        CatsnapAuthority authority = CatsnapAuthority.MODEL;
        long beforeIssueTime = System.currentTimeMillis();

        // when
        String token = jwtAccessTokenManager.issue(userId, authority);

        // then
        Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();

        // 발급 시간이 현재 시간 근처인지 확인 (1초 오차 허용)
        assertThat(issuedAt.getTime()).isBetween(beforeIssueTime - 1000, beforeIssueTime + 1000);

        // 만료 시간이 발급 시간 + expirationMinutes인지 확인 (1초 오차 허용)
        long expectedExpiration = issuedAt.getTime() + EXPIRATION_MINUTES * 60 * 1000;
        assertThat(expiration.getTime()).isBetween(expectedExpiration - 1000,
            expectedExpiration + 1000);
    }

    @Test
    void 다른_userId와_authority로_토큰을_발급하면_다른_토큰이_생성된다() {
        // given
        Long userId1 = 1L;
        Long userId2 = 2L;
        CatsnapAuthority authority1 = CatsnapAuthority.PHOTOGRAPHER;
        CatsnapAuthority authority2 = CatsnapAuthority.MODEL;

        // when
        String token1 = jwtAccessTokenManager.issue(userId1, authority1);
        String token2 = jwtAccessTokenManager.issue(userId2, authority2);

        // then
        assertThat(token1).isNotEqualTo(token2);
    }
}