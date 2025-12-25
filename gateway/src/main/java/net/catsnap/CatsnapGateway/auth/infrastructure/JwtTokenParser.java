package net.catsnap.CatsnapGateway.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.domain.TokenParser;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import org.springframework.stereotype.Component;

/**
 * jjwt 라이브러리를 사용하여 JWT 토큰을 파싱하는 인프라 구현체입니다. TokenParser 인터페이스를 구현하여 도메인 레이어와 인프라 레이어를 분리합니다.
 */
@Component
@RequiredArgsConstructor
public class JwtTokenParser implements TokenParser {

    private final SecretKey secretKey;

    /**
     * JWT 토큰을 파싱하여 클레임 정보를 추출합니다. 토큰의 형식이 잘못되었거나, 서명이 유효하지 않거나, 만료된 경우 빈 Optional을 반환합니다.
     *
     * @param token 파싱할 토큰
     * @return 파싱된 클레임 정보를 담은 Optional. 파싱 실패 시 Optional.empty()
     */
    @Override
    public Optional<TokenClaims> parse(Token token) {
        try {
            Claims claims = parseJwtClaims(token.value());
            return extractTokenClaims(claims);
        } catch (ExpiredJwtException e) {
            // TODO: 리프레시 토큰 발행 API 호출
            return Optional.empty();
        } catch (JwtException e) {
            return Optional.empty();
        }
    }

    /**
     * JWT 문자열을 파싱하여 Claims를 추출합니다.
     *
     * @param tokenValue JWT 토큰 문자열
     * @return 파싱된 Claims
     * @throws JwtException JWT 파싱 실패 시
     */
    private Claims parseJwtClaims(String tokenValue) {
        JwtParser jwtParser = Jwts.parser()
            .verifyWith(secretKey)
            .build();
        return jwtParser.parseSignedClaims(tokenValue).getPayload();
    }

    /**
     * Claims에서 도메인 객체인 TokenClaims를 추출합니다. userId 또는 authority가 없으면 빈 Optional을 반환합니다.
     *
     * @param claims JWT Claims
     * @return TokenClaims를 담은 Optional
     */
    private Optional<TokenClaims> extractTokenClaims(Claims claims) {
        Long userId = claims.get("id", Long.class);
        String authority = claims.get("authority", String.class);

        if (userId == null || authority == null || authority.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(new TokenClaims(userId, authority));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
