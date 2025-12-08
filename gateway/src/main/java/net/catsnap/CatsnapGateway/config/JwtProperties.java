package net.catsnap.CatsnapGateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * JWT 관련 설정 프로퍼티를 관리하는 클래스
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security")
public class JwtProperties {

    /**
     * JWT 서명에 사용되는 비밀 키
     */
    private String jwtKey;
}