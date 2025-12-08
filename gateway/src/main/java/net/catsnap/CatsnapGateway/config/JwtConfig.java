package net.catsnap.CatsnapGateway.config;

import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT 관련 빈을 생성하는 설정 클래스
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtProperties jwtProperties;

    /**
     * JWT 서명에 사용할 SecretKey 빈을 생성합니다.
     *
     * @return HMAC SHA 알고리즘을 사용하는 SecretKey
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getJwtKey().getBytes(StandardCharsets.UTF_8));
    }
}
