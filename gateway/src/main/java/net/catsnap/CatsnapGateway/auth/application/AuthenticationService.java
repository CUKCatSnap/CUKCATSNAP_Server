package net.catsnap.CatsnapGateway.auth.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.domain.TokenParser;
import net.catsnap.CatsnapGateway.auth.domain.vo.Passport;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증을 처리하는 Application Service입니다. HTTP 요청에서 토큰을 추출하고 파싱하여 사용자 인증 정보(Passport)를 생성합니다.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenExtractor tokenExtractor;
    private final TokenParser tokenParser;

    /**
     * HTTP 요청에서 사용자 인증 정보를 추출하여 Passport를 생성합니다. 토큰이 없거나 유효하지 않으면 익명 사용자 Passport를 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 사용자 인증 정보를 담은 Passport
     */
    public Passport authenticate(ServerHttpRequest request) {
        // 요청에서 토큰 추출
        Optional<Token> tokenOptional = tokenExtractor.extract(request);
        if (tokenOptional.isEmpty()) {
            return Passport.createAnonymous();
        }

        // 토큰 파싱하여 클레임 추출
        Optional<TokenClaims> claimsOptional = tokenParser.parse(tokenOptional.get());
        if (claimsOptional.isEmpty()) {
            return Passport.createAnonymous();
        }

        // 클레임에서 Passport 생성
        return createPassport(claimsOptional.get());
    }

    /**
     * TokenClaims를 기반으로 Passport를 생성합니다. 권한 정보가 유효하지 않으면 익명 사용자 Passport를 반환합니다.
     *
     * @param claims 토큰 클레임 정보
     * @return 생성된 Passport
     */
    private Passport createPassport(TokenClaims claims) {
        Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(
            claims.authority());

        return authority
            .map(auth -> new Passport(claims.userId(), auth))
            .orElse(Passport.createAnonymous());
    }
}
