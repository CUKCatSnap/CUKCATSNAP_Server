package net.catsnap.CatsnapGateway.auth.service;

import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * JWT 토큰에서 사용자 인증 정보를 추출하는 서비스 클래스입니다. 이 클래스는 HTTP 요청에서 JWT를 추출하고, 유효한 경우 사용자 정보를 생성합니다.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

    private static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenParser jwtTokenParser;

    /**
     * HTTP 요청에서 JWT 토큰을 추출하고 파싱하여 사용자 인증 정보를 반환합니다. 토큰이 유효하면 해당 사용자의 인증 정보를, 그렇지 않으면 익명 사용자의 인증 정보를
     * 반환합니다.
     *
     * @param serverHttpRequest HTTP 요청 객체 (ServerHttpRequest)
     * @return 사용자 인증 정보 (UserAuthInformation).
     */
    @Override
    public UserAuthInformation getUserAuthInformation(ServerHttpRequest serverHttpRequest) {
        // 1. 요청에서 토큰 추출
        Optional<String> tokenOptional = extractJwtToken(serverHttpRequest);
        if (tokenOptional.isEmpty()) {
            return getAnonymousUserAuthInformation();
        }

        // 2. 토큰 파싱하여 클레임(정보) 추출
        Optional<Claims> claimsOptional = jwtTokenParser.parseClaims(tokenOptional.get());
        if (claimsOptional.isEmpty()) {
            return getAnonymousUserAuthInformation();
        } else {
            return createUserAuthInfoFrom(claimsOptional.get());
        }
    }

    /**
     * JWT 토큰의 클레임(Claims)을 기반으로 사용자 인증 정보(UserAuthInformation)를 생성합니다.
     *
     * @param claims JWT에서 추출한 클레임 객체
     * @return 생성된 사용자 인증 정보. 필수 클레임이 없으면 익명 사용자 정보를 반환합니다.
     */
    private UserAuthInformation createUserAuthInfoFrom(Claims claims) {
        Long id = claims.get("id", Long.class);
        List<String> authorities = claims.get("authorities", List.class);

        if (id == null || authorities == null || authorities.isEmpty()) {
            return getAnonymousUserAuthInformation();
        }

        return new UserAuthInformation(id, authorities.get(0));
    }

    /**
     * HTTP 요청 헤더에서 "Bearer " 접두사를 제외한 순수 JWT 토큰을 추출합니다.
     *
     * @param request HTTP 요청 객체 (ServerHttpRequest)
     * @return 추출된 JWT 토큰을 담은 Optional 객체. 토큰이 없으면 Optional.empty()
     */
    private Optional<String> extractJwtToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.of(authHeader.substring(BEARER_PREFIX.length()));
        }
        return Optional.empty();
    }

    /**
     * 익명 사용자 인증 정보를 생성하여 반환합니다.
     *
     * @return 익명 사용자 인증 정보 (UserAuthInformation)
     */
    private UserAuthInformation getAnonymousUserAuthInformation() {
        return new UserAuthInformation(-1L, "ANONYMOUS");
    }
}
