package net.catsnap.CatsnapGateway.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.TokenParser;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import net.catsnap.CatsnapGateway.auth.domain.vo.TokenClaims;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationServiceTest {

    @Mock
    private TokenExtractor tokenExtractor;

    @Mock
    private TokenParser tokenParser;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Nested
    class AuthenticateWithValidToken {

        @Test
        void 사진작가_권한을_가진_사용자로_인증한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("valid.jwt.token");
            TokenClaims claims = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            TokenClaims result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.PHOTOGRAPHER);
            assertThat(result.isAuthenticated()).isTrue();
        }

        @Test
        void 모델_권한을_가진_사용자로_인증한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("valid.jwt.token");
            TokenClaims claims = new TokenClaims(2L, CatsnapAuthority.MODEL);

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.of(claims));

            // when
            TokenClaims result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(2L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.MODEL);
            assertThat(result.isAuthenticated()).isTrue();
        }
    }

    @Nested
    class NoToken {

        @Test
        void 익명_사용자_TokenClaims_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            given(tokenExtractor.extract(request)).willReturn(Optional.empty());

            // when
            TokenClaims result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(-1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
            assertThat(result.isAnonymous()).isTrue();
        }
    }

    @Nested
    class 토큰이_올바르지_않을_때 {

        @Test
        void 익명_사용자_TokenClaims_파싱_실패시_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest.get("/test").build();
            Token token = new Token("invalid.jwt.token");

            given(tokenExtractor.extract(request)).willReturn(Optional.of(token));
            given(tokenParser.parse(token)).willReturn(Optional.empty());

            // when
            TokenClaims result = authenticationService.authenticate(request);

            // then
            assertThat(result.userId()).isEqualTo(-1L);
            assertThat(result.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
            assertThat(result.isAnonymous()).isTrue();
        }
    }
}
