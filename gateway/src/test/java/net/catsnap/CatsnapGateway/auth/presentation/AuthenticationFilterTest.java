package net.catsnap.CatsnapGateway.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import net.catsnap.CatsnapGateway.auth.application.AuthenticationService;
import net.catsnap.CatsnapGateway.auth.application.PassportService;
import net.catsnap.CatsnapGateway.auth.domain.vo.Passport;
import net.catsnap.CatsnapGateway.auth.dto.AuthenticationPassport;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationFilter 테스트")
class AuthenticationFilterTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PassportService passportService;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private GatewayFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filterChain = mock(GatewayFilterChain.class);
        given(filterChain.filter(any(ServerWebExchange.class))).willReturn(Mono.empty());
    }

    @Nested
    @DisplayName("JWT 토큰이 없는 경우")
    class NoToken {

        @Test
        @DisplayName("익명 사용자로 처리되고 익명 Passport가 발급된다")
        void processAsAnonymousUser() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            ServerHttpRequest invalidatedRequest = request.mutate().build();
            Passport anonymousPassport = Passport.createAnonymous();
            AuthenticationPassport anonymousAuthPassport = new AuthenticationPassport(-1L,
                CatsnapAuthority.ANONYMOUS);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-User-Id", "-1")
                .header("X-Authority", "ANONYMOUS")
                .build();

            given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
            given(authenticationService.authenticate(invalidatedRequest)).willReturn(
                anonymousPassport);
            given(passportService.issuePassport(invalidatedRequest, anonymousAuthPassport))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(passportService).invalidatePassport(request);
            verify(authenticationService).authenticate(invalidatedRequest);
            verify(passportService).issuePassport(invalidatedRequest, anonymousAuthPassport);
            verify(filterChain).filter(any(ServerWebExchange.class));
        }
    }

    @Nested
    @DisplayName("유효한 JWT 토큰이 있는 경우")
    class ValidToken {

        @Test
        @DisplayName("사용자 인증 정보가 추출되고 Passport가 발급된다")
        void extractAuthenticationAndIssuePassport() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer valid-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            ServerHttpRequest invalidatedRequest = request.mutate().build();
            Passport authenticatedPassport = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);
            AuthenticationPassport authPassport = new AuthenticationPassport(1L,
                CatsnapAuthority.PHOTOGRAPHER);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-User-Id", "1")
                .header("X-Authority", "photographer")
                .build();

            given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
            given(authenticationService.authenticate(invalidatedRequest)).willReturn(
                authenticatedPassport);
            given(passportService.issuePassport(invalidatedRequest, authPassport))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(passportService).invalidatePassport(request);
            verify(authenticationService).authenticate(invalidatedRequest);
            verify(passportService).issuePassport(invalidatedRequest, authPassport);
            verify(filterChain).filter(any(ServerWebExchange.class));
        }

        @Test
        @DisplayName("MODEL 권한을 가진 사용자로 인증된다")
        void authenticateAsModelUser() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer model-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            ServerHttpRequest invalidatedRequest = request.mutate().build();
            Passport modelPassport = new Passport(2L, CatsnapAuthority.MODEL);
            AuthenticationPassport authPassport = new AuthenticationPassport(2L,
                CatsnapAuthority.MODEL);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-User-Id", "2")
                .header("X-Authority", "model")
                .build();

            given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
            given(authenticationService.authenticate(invalidatedRequest)).willReturn(modelPassport);
            given(passportService.issuePassport(invalidatedRequest, authPassport))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(authenticationService).authenticate(invalidatedRequest);
            verify(passportService).issuePassport(invalidatedRequest, authPassport);
        }
    }

    @Nested
    @DisplayName("사용자가 임의로 Passport를 주입한 경우")
    class InjectedPassport {

        @Test
        @DisplayName("주입된 Passport가 무효화되고 실제 인증 정보로 새로운 Passport가 발급된다")
        void invalidateInjectedPassportAndIssueRealOne() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("X-User-Id", "999")  // 사용자가 임의로 주입
                .header("X-Authority", "ADMIN")  // 사용자가 임의로 주입
                .header("Authorization", "Bearer valid-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            // 임의로 주입된 헤더가 제거된 요청
            ServerHttpRequest invalidatedRequest = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer valid-jwt-token")
                .build();

            Passport actualPassport = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);
            AuthenticationPassport actualAuthPassport = new AuthenticationPassport(1L,
                CatsnapAuthority.PHOTOGRAPHER);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-User-Id", "1")
                .header("X-Authority", "photographer")
                .build();

            given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
            given(authenticationService.authenticate(invalidatedRequest)).willReturn(
                actualPassport);
            given(passportService.issuePassport(invalidatedRequest, actualAuthPassport))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            // 기존 패스포트가 무효화되었는지 확인
            verify(passportService).invalidatePassport(request);
            // 무효화된 요청으로 인증이 수행되었는지 확인
            verify(authenticationService).authenticate(invalidatedRequest);
            // 실제 인증 정보로 새로운 패스포트가 발급되었는지 확인
            verify(passportService).issuePassport(invalidatedRequest, actualAuthPassport);

            // 수정된 exchange로 필터 체인이 계속되는지 확인
            ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(
                ServerWebExchange.class);
            verify(filterChain).filter(exchangeCaptor.capture());

            ServerWebExchange capturedExchange = exchangeCaptor.getValue();
            assertThat(capturedExchange.getRequest()).isEqualTo(passportIssuedRequest);
        }
    }
}
