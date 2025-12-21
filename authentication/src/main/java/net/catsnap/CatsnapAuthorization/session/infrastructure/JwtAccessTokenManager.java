package net.catsnap.CatsnapAuthorization.session.infrastructure;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import net.catsnap.CatsnapAuthorization.session.domain.AccessTokenManager;
import net.catsnap.shared.auth.CatsnapAuthority;

/**
 * JWT 기반 액세스 토큰 발급 구현 클래스 (Infrastructure Layer)
 */
public class JwtAccessTokenManager implements AccessTokenManager {

    private final SecretKey secretKey;
    private final Long expirationMinutes;

    /**
     * JwtAccessTokenIssuer 생성자
     *
     * @param secretKey         JWT 서명에 사용할 비밀 키
     * @param expirationMinutes 액세스 토큰 만료 시간 (분 단위)
     */
    public JwtAccessTokenManager(SecretKey secretKey, Long expirationMinutes) {
        this.secretKey = secretKey;
        this.expirationMinutes = expirationMinutes;
    }

    @Override
    public String issue(Long userId, CatsnapAuthority authority) {
        return Jwts.builder()
            .header()
            .add("provider", "catsnap")
            .add("type", "accessToken")
            .and()
            .claims(Map.of(
                "id", userId,
                "authority", authority.getAuthorityName()
            ))
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMinutes * 60L * 1000L))
            .signWith(secretKey)
            .compact();
    }
}