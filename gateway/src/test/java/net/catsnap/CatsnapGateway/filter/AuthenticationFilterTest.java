package net.catsnap.CatsnapGateway.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import net.catsnap.CatsnapGateway.auth.dto.UserAuthInformation;
import net.catsnap.CatsnapGateway.auth.service.PassportService;
import net.catsnap.CatsnapGateway.auth.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
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
    private TokenService tokenService;

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

    @Test
    void JWT_토큰이_없을_때_익명_사용자로_처리된다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        ServerHttpRequest invalidatedPassportRequest = request.mutate().build();
        UserAuthInformation anonymousUser = new UserAuthInformation(-1L, "ANONYMOUS");
        ServerHttpRequest passportIssuedRequest = request.mutate()
            .header("X-User-Id", "-1")
            .header("X-Authority", "ANONYMOUS")
            .build();

        given(passportService.invalidatePassport(request)).willReturn(invalidatedPassportRequest);
        given(tokenService.getUserAuthInformation(invalidatedPassportRequest)).willReturn(
            anonymousUser);
        given(passportService.issuePassport(invalidatedPassportRequest, anonymousUser))
            .willReturn(passportIssuedRequest);

        // when
        authenticationFilter.filter(exchange, filterChain);

        // then
        verify(passportService).invalidatePassport(request);
        verify(tokenService).getUserAuthInformation(invalidatedPassportRequest);
        verify(passportService).issuePassport(invalidatedPassportRequest, anonymousUser);
        verify(filterChain).filter(any(ServerWebExchange.class));
    }

    @Test
    void JWT_토큰이_있을_때_사용자_인증_정보가_추출되고_패스포트가_발급된다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header("Authorization", "Bearer valid-jwt-token")
            .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        ServerHttpRequest invalidatedRequest = request.mutate().build();
        UserAuthInformation authenticatedUser = new UserAuthInformation(1L, "MEMBER");
        ServerHttpRequest passportIssuedRequest = request.mutate()
            .header("X-User-Id", "1")
            .header("X-Authority", "MEMBER")
            .build();

        given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
        given(tokenService.getUserAuthInformation(invalidatedRequest)).willReturn(
            authenticatedUser);
        given(passportService.issuePassport(invalidatedRequest, authenticatedUser))
            .willReturn(passportIssuedRequest);

        // when
        authenticationFilter.filter(exchange, filterChain);

        // then
        verify(passportService).invalidatePassport(request);
        verify(tokenService).getUserAuthInformation(invalidatedRequest);
        verify(passportService).issuePassport(invalidatedRequest, authenticatedUser);
        verify(filterChain).filter(any(ServerWebExchange.class));
    }

    @Test
    void 사용자가_임의로_패스포트를_입력했을_때_무효화되고_새로운_패스포트가_발급된다() {
        // given
        MockServerHttpRequest request = MockServerHttpRequest.get("/test")
            .header("X-User-Id", "999")
            .header("X-Authority", "ADMIN")
            .header("Authorization", "Bearer valid-jwt-token")
            .build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);

        // 임의로 주입된 패스포트가 무효화된 요청
        ServerHttpRequest invalidatedRequest = MockServerHttpRequest.get("/test")
            .header("Authorization", "Bearer valid-jwt-token")
            .build();

        UserAuthInformation actualUser = new UserAuthInformation(1L, "MEMBER");
        ServerHttpRequest passportIssuedRequest = request.mutate()
            .header("X-User-Id", "1")
            .header("X-Authority", "MEMBER")
            .build();

        given(passportService.invalidatePassport(request)).willReturn(invalidatedRequest);
        given(tokenService.getUserAuthInformation(invalidatedRequest)).willReturn(actualUser);
        given(passportService.issuePassport(invalidatedRequest, actualUser))
            .willReturn(passportIssuedRequest);

        // when
        authenticationFilter.filter(exchange, filterChain);

        // then
        // 기존 패스포트가 무효화되었는지 확인
        verify(passportService).invalidatePassport(request);
        // 무효화된 요청으로 사용자 정보를 가져왔는지 확인
        verify(tokenService).getUserAuthInformation(invalidatedRequest);
        // 실제 사용자 정보로 새로운 패스포트가 발급되었는지 확인
        verify(passportService).issuePassport(invalidatedRequest, actualUser);

        // 수정된 exchange로 필터 체인이 계속되는지 확인
        ArgumentCaptor<ServerWebExchange> exchangeCaptor = ArgumentCaptor.forClass(
            ServerWebExchange.class);
        verify(filterChain).filter(exchangeCaptor.capture());

        ServerWebExchange capturedExchange = exchangeCaptor.getValue();
        assertThat(capturedExchange.getRequest()).isEqualTo(passportIssuedRequest);
    }
}