package net.catsnap.CatsnapGateway.filter;

import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import net.catsnap.CatsnapGateway.auth.service.PassportService;
import net.catsnap.CatsnapGateway.auth.service.TokenService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 인증 필터 클래스. 모든 요청에 대해 사용자 인증 정보를 처리하고, Passport를 발급하거나 무효화합니다.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final TokenService tokenService;
    private final PassportService passportService;

    /**
     * 필터 로직을 수행합니다. 요청에서 기존 Passport를 무효화하고, 토큰 서비스로부터 사용자 인증 정보를 얻어 새로운 Passport를 발급합니다.
     *
     * @param exchange 현재 웹 요청 및 응답을 나타내는 ServerWebExchange 객체.
     * @param chain    다음 필터 또는 핸들러로 요청을 전달하는 GatewayFilterChain 객체.
     * @return 필터 체인의 다음 단계로 진행하는 Mono<Void> 객체.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        passportService.invalidatePassport(exchange.getRequest());

        UserAuthInformation userAuthInformation = tokenService.getUserAuthInformation(
            exchange.getRequest());

        passportService.issuePassport(exchange.getRequest(), userAuthInformation);

        return chain.filter(exchange);
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
