package net.catsnap.CatsnapGateway.auth.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import net.catsnap.CatsnapGateway.auth.application.AuthenticationService;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import net.catsnap.CatsnapGateway.passport.application.PassportIssuer;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationFilterTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PassportIssuer passportIssuer;

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    private GatewayFilterChain filterChain;

    @BeforeEach
    void setUp() {
        filterChain = mock(GatewayFilterChain.class);
        given(filterChain.filter(any(ServerWebExchange.class))).willReturn(Mono.empty());
    }

    @Nested
    class JWT_토큰이_없는_경우 {

        @Test
        void 익명_사용자로_처리되고_익명_Passport가_발급된다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            TokenClaims anonymousClaims = TokenClaims.anonymous();
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-Passport", "signed-passport-data")
                .build();

            given(authenticationService.authenticate(request)).willReturn(anonymousClaims);
            given(
                passportIssuer.issuePassport(eq(request), eq(-1L), eq(CatsnapAuthority.ANONYMOUS)))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(authenticationService).authenticate(request);
            verify(passportIssuer).issuePassport(request, -1L, CatsnapAuthority.ANONYMOUS);
            verify(filterChain).filter(any(ServerWebExchange.class));
        }
    }

    @Nested
    class 유효한_JWT_토큰이_있는_경우 {

        @Test
        void 사용자_인증_정보가_추출되고_Passport가_발급된다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer valid-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            TokenClaims photographerClaims = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-Passport", "signed-passport-data")
                .build();

            given(authenticationService.authenticate(request)).willReturn(photographerClaims);
            given(passportIssuer.issuePassport(eq(request), eq(1L),
                eq(CatsnapAuthority.PHOTOGRAPHER)))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(authenticationService).authenticate(request);
            verify(passportIssuer).issuePassport(request, 1L, CatsnapAuthority.PHOTOGRAPHER);
            verify(filterChain).filter(any(ServerWebExchange.class));
        }

        @Test
        void MODEL_권한을_가진_사용자로_인증된다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("Authorization", "Bearer model-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            TokenClaims modelClaims = new TokenClaims(2L, CatsnapAuthority.MODEL);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-Passport", "signed-passport-data")
                .build();

            given(authenticationService.authenticate(request)).willReturn(modelClaims);
            given(passportIssuer.issuePassport(eq(request), eq(2L), eq(CatsnapAuthority.MODEL)))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            verify(authenticationService).authenticate(request);
            verify(passportIssuer).issuePassport(request, 2L, CatsnapAuthority.MODEL);
        }
    }

    @Nested
    class 사용자가_임의로_Passport를_주입한_경우 {

        @Test
        void 주입된_Passport가_무효화되고_실제_인증정보로_새로운_Passport가_발급된다() {
            // given
            MockServerHttpRequest request = MockServerHttpRequest.get("/test")
                .header("X-Passport", "fake-passport-data")  // 사용자가 임의로 주입
                .header("Authorization", "Bearer valid-jwt-token")
                .build();
            ServerWebExchange exchange = MockServerWebExchange.from(request);

            TokenClaims actualClaims = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);
            ServerHttpRequest passportIssuedRequest = request.mutate()
                .header("X-Passport", "real-signed-passport-data")
                .build();

            given(authenticationService.authenticate(request)).willReturn(actualClaims);
            given(passportIssuer.issuePassport(eq(request), eq(1L),
                eq(CatsnapAuthority.PHOTOGRAPHER)))
                .willReturn(passportIssuedRequest);

            // when
            authenticationFilter.filter(exchange, filterChain);

            // then
            // 인증이 수행되었는지 확인
            verify(authenticationService).authenticate(request);
            // 실제 인증 정보로 새로운 패스포트가 발급되었는지 확인 (내부적으로 기존 Passport 무효화)
            verify(passportIssuer).issuePassport(request, 1L, CatsnapAuthority.PHOTOGRAPHER);

            // 수정된 exchange로 필터 체인이 계속되는지 확인
            ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(
                ServerWebExchange.class);
            verify(filterChain).filter(exchangeCaptor.capture());

            ServerWebExchange capturedExchange = exchangeCaptor.getValue();
            assertThat(capturedExchange.getRequest()).isEqualTo(passportIssuedRequest);
        }
    }
}
