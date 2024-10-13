package com.cuk.catsnap.global.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class Util {

    @Value("${spring.security.jwt-key}")
    private String jwtKey;

    /*
     *  로그인 처리 과정에서  request의 body값을 읽을 때 필요한 ObjectMapper 빈 등록
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /*
        *  JWT 토큰 생성 시 사용할 SecretKey 빈 등록
     */
    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtKey.getBytes());
    }

    @Bean
    public ServletSecurityResponse servletResponse(ObjectMapper objectMapper, SecretKey key) {
        return new ServletSecurityResponse(objectMapper, key);
    }
}
