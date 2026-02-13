package net.catsnap.CatsnapGateway.auth.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.domain.TokenParser;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 처리하는 Application Service입니다. HTTP 요청에서 JWT 토큰을 추출하고 파싱하여 사용자 인증 정보를 추출합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenExtractor tokenExtractor;
    private final TokenParser tokenParser;

    /**
     * HTTP 요청에서 JWT 토큰을 추출하고 파싱하여 클레임 정보를 반환합니다. 토큰이 없거나 유효하지 않으면 익명 사용자 클레임을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return JWT에서 추출한 클레임 정보 (userId, authority)
     */
    public TokenClaims authenticate(ServerHttpRequest request) {
        // 요청에서 토큰 추출
        Optional<Token> tokenOptional = tokenExtractor.extract(request);
        if (tokenOptional.isEmpty()) {
            return TokenClaims.anonymous();
        }

        // 토큰 파싱하여 클레임 추출
        Optional<TokenClaims> claimsOptional = tokenParser.parse(tokenOptional.get());
        return claimsOptional.orElse(TokenClaims.anonymous());
    }
}
