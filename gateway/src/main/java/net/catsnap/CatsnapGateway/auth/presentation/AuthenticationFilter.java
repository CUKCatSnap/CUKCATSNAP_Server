package net.catsnap.CatsnapGateway.auth.presentation;

import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.application.AuthenticationService;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import net.catsnap.CatsnapGateway.passport.application.PassportIssuer;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 인증 필터 클래스. 모든 요청에 대해 사용자 인증 정보를 처리하고, 서명된 Passport를 발급합니다.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final AuthenticationService authenticationService;
    private final PassportIssuer passportIssuer;

    /**
     * 필터 로직을 수행합니다. JWT에서 클레임 정보를 추출하고 서명된 Passport를 발급합니다.
     *
     * @param exchange 현재 웹 요청 및 응답을 나타내는 ServerWebExchange 객체.
     * @param chain    다음 필터 또는 핸들러로 요청을 전달하는 GatewayFilterChain 객체.
     * @return 필터 체인의 다음 단계로 진행하는 Mono<Void> 객체.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. JWT에서 클레임 정보 추출
        TokenClaims claims = authenticationService.authenticate(exchange.getRequest());

        // 2. 클레임 정보를 기반으로 서명된 Passport 발급 (내부적으로 기존 Passport 무효화 수행)
        ServerHttpRequest newRequest = passportIssuer.issuePassport(
            exchange.getRequest(),
            claims.userId(),
            claims.authority()
        );

        ServerWebExchange modifiedExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(modifiedExchange);
    }

    /**
     * 필터의 실행 순서를 반환합니다. 이 필터는 가장 높은 우선순위로 실행됩니다.
     *
     * @return 필터의 실행 순서를 나타내는 정수 값.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
