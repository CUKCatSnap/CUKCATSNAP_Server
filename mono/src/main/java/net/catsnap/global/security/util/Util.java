package net.catsnap.global.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Util {

    @Value("${spring.security.jwt-key}")
    private String jwtKey;

    /*
     *  JWT 토큰 생성 시 사용할 SecretKey 빈 등록
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public ServletSecurityResponse servletResponse(ObjectMapper objectMapper) {
        return new ServletSecurityResponse(objectMapper);
    }
}
