package net.catsnap.CatsnapGateway.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JWT 토큰을 파싱하여 Claims를 추출하는 클래스입니다. JWT 토큰의 검증과 파싱만을 담당합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenParser {

    private final SecretKey secretKey;

    /**
     * JWT 토큰 문자열을 파싱하여 클레임(Claims)을 Optional 객체로 반환합니다. 토큰의 형식이 잘못되었거나, 서명이 유효하지 않거나, 만료된 경우 빈
     * Optional을 반환합니다.
     *
     * @param token 파싱할 JWT 토큰 문자열
     * @return 파싱된 클레임을 담은 Optional 객체. 파싱 실패 시 Optional.empty()
     */
    public Optional<Claims> parseClaims(String token) {
        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
            return Optional.of(jwtParser.parseClaimsJws(token).getBody());
        } catch (ExpiredJwtException e) {
            // TODO: 리프레시 토큰 발행 로직 구현
            return Optional.empty();
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}
