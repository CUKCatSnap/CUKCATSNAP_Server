package net.catsnap.CatsnapAuthorization.session.infrastructure;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapAuthorization.session.domain.AccessTokenManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 관련 설정 클래스
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    /**
     * JWT 서명에 사용할 SecretKey를 생성합니다.
     *
     * @return SecretKey 객체
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getJwtKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * JWT 기반 액세스 토큰 발급자를 생성합니다.
     *
     * @param secretKey JWT 서명에 사용할 비밀 키
     * @return AccessTokenIssuer 구현체
     */
    @Bean
    public AccessTokenManager accessTokenIssuer(SecretKey secretKey) {
        return new JwtAccessTokenManager(
            secretKey,
            jwtProperties.getExpiration()
        );
    }
}