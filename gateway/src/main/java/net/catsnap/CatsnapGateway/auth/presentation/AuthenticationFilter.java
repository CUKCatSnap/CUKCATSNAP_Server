package net.catsnap.CatsnapGateway.auth.presentation;

import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapGateway.auth.application.AuthenticationService;
import net.catsnap.CatsnapGateway.auth.application.PassportService;
import net.catsnap.CatsnapGateway.auth.domain.vo.Passport;
import net.catsnap.CatsnapGateway.auth.dto.AuthenticationPassport;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 인증 필터 클래스. 모든 요청에 대해 사용자 인증 정보를 처리하고, Passport를 발급하거나 무효화합니다. DDD 구조에 따라 Application Service를
 * 사용하여 인증 처리를 수행합니다.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final AuthenticationService authenticationService;
    private final PassportService passportService;

    /**
     * 필터 로직을 수행합니다. 요청에서 기존 Passport를 무효화하고, 인증 서비스로부터 사용자 인증 정보를 얻어 새로운 Passport를 발급합니다.
     *
     * @param exchange 현재 웹 요청 및 응답을 나타내는 ServerWebExchange 객체.
     * @param chain    다음 필터 또는 핸들러로 요청을 전달하는 GatewayFilterChain 객체.
     * @return 필터 체인의 다음 단계로 진행하는 Mono<Void> 객체.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 기존 Passport 헤더 무효화
        ServerHttpRequest invalidatedRequest = passportService.invalidatePassport(
            exchange.getRequest());

        // 2. 인증 서비스를 통해 사용자 인증 처리
        Passport passport = authenticationService.authenticate(invalidatedRequest);

        // 3. 도메인 객체를 DTO로 변환 (PassportService는 추후 리팩토링 예정)
        AuthenticationPassport authenticationPassport = toAuthenticationPassport(passport);

        // 4. 새로운 Passport 헤더 발급
        ServerHttpRequest newRequest = passportService.issuePassport(invalidatedRequest,
            authenticationPassport);

        ServerWebExchange modifiedExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(modifiedExchange);
    }

    /**
     * 도메인 객체인 Passport를 DTO인 AuthenticationPassport로 변환합니다.
     * TODO: PassportService 리팩토링 시 이 메서드는 제거될 예정입니다.
     *
     * @param passport 도메인 Passport 객체
     * @return AuthenticationPassport DTO
     */
    private AuthenticationPassport toAuthenticationPassport(Passport passport) {
        return new AuthenticationPassport(passport.userId(), passport.authority());
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
