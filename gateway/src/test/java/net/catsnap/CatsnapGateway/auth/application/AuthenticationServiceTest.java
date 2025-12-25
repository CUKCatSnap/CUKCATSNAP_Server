package net.catsnap.CatsnapGateway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.TokenParser;
import net.catsnap.CatsnapGateway.auth.domain.vo.Passport;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService 테스트")
class AuthenticationServiceTest {

    @Mock
    private TokenExtractor tokenExtractor;

    @Mock
    private TokenParser tokenParser;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Nested
    @DisplayName("유효한 토큰으로 인증 시")
    class AuthenticateWithValidToken {

        @Test
        @DisplayName("PHOTOGRAPHER 권한을 가진 사용자로 인증한다")
        void authenticatePhotographer() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("valid.jwt.token");
            TokenClaims claims = new TokenClaims(1L, "PHOTOGRAPHER");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.PHOTOGRAPHER);
            assertThat(result.isAuthenticated()).isTrue();
        }

        @Test
        @DisplayName("MODEL 권한을 가진 사용자로 인증한다")
        void authenticateModel() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("valid.jwt.token");
            TokenClaims claims = new TokenClaims(2L, "MODEL");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(2L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.MODEL);
            assertThat(result.isAuthenticated()).isTrue();
        }
    }

    @Nested
    @DisplayName("토큰이 없는 경우")
    class NoToken {

        @Test
        @DisplayName("익명 사용자 Passport를 반환한다")
        void returnAnonymousPassport() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            given(tokenExtractor.extract(request)).willReturn(Optional.empty());

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(-1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
            assertThat(result.isAnonymous()).isTrue();
        }
    }

    @Nested
    @DisplayName("토큰 파싱 실패 시")
    class TokenParsingFailed {

        @Test
        @DisplayName("익명 사용자 Passport를 반환한다")
        void returnAnonymousPassportWhenParsingFails() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("invalid.jwt.token");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.empty());

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(-1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
            assertThat(result.isAnonymous()).isTrue();
        }
    }

    @Nested
    @DisplayName("유효하지 않은 권한인 경우")
    class InvalidAuthority {

        @Test
        @DisplayName("익명 사용자 Passport를 반환한다")
        void returnAnonymousPassportWhenInvalidAuthority() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("valid.jwt.token");
            TokenClaims claims = new TokenClaims(1L, "INVALID_AUTHORITY");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(-1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
            assertThat(result.isAnonymous()).isTrue();
        }
    }

    @Nested
    @DisplayName("통합 시나리오")
    class IntegrationScenario {

        @Test
        @DisplayName("전체 인증 플로우가 정상 동작한다")
        void fullAuthenticationFlow() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/api/photos").build();
            Token token = new Token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9");
            TokenClaims claims = new TokenClaims(100L, "PHOTOGRAPHER");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            Passport result = authenticationService.authenticate(request);

            // then
            assertThat(result).isNotNull();
            assertThat(result.userId()).isEqualTo(100L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.PHOTOGRAPHER);
            assertThat(result.isAuthenticated()).isTrue();
            assertThat(result.isAnonymous()).isFalse();
        }
    }
}
