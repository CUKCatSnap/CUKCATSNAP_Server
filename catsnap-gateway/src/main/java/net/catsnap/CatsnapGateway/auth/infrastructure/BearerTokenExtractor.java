package net.catsnap.CatsnapGateway.auth.infrastructure;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.application.TokenExtractor;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * HTTP Authorization 헤더에서 Bearer 토큰을 추출하는 인프라 구현체입니다.
 * TokenExtractor 인터페이스를 구현하여 Application 레이어와 Infrastructure 레이어를 분리합니다.
 */
@Component
public class BearerTokenExtractor implements TokenExtractor {

    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * HTTP 요청의 Authorization 헤더에서 Bearer 토큰을 추출합니다.
     * Authorization 헤더가 없거나 Bearer 형식이 아닌 경우 빈 Optional을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 추출된 토큰을 담은 Optional. 토큰이 없으면 Optional.empty()
     */
    @Override
    public Optional<Token> extract(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return Optional.empty();
        }

        String tokenValue = authHeader.substring(BEARER_PREFIX.length());

        try {
            return Optional.of(new Token(tokenValue));
        } catch (IllegalArgumentException e) {
            // 토큰 값이 null이거나 빈 문자열인 경우
            return Optional.empty();
        }
    }
}
