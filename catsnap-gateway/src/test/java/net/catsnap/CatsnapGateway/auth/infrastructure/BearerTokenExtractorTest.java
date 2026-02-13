package net.catsnap.CatsnapGateway.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BearerTokenExtractorTest {

    private BearerTokenExtractor bearerTokenExtractor;

    @BeforeEach
    void setUp() {
        bearerTokenExtractor = new BearerTokenExtractor();
    }

    @Nested
    class 유효한_Bearer_토큰_추출_시 {

        @Test
        void Authorization_헤더에서_Bearer_토큰을_추출한다() {
            // given
            String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0";
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().value()).isEqualTo(tokenValue);
        }

        @Test
        void 긴_Bearer_토큰을_추출한다() {
            // given
            String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().value()).isEqualTo(tokenValue);
        }
    }

    @Nested
    class Authorization_헤더가_없는_경우 {

        @Test
        void 빈_Optional을_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class Bearer_접두사가_없는_경우 {

        @Test
        void 토큰만_있는_경우_빈_Optional을_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void 다른_인증_방식_Basic인_경우_빈_Optional을_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNzd29yZA==")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class Bearer_뒤에_토큰이_없는_경우 {

        @Test
        void Bearer만_있는_경우_빈_Optional을_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void Bearer_뒤에_공백만_있는_경우_빈_Optional을_반환한다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer    ")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    class 대소문자_구분 {

        @Test
        void 소문자_bearer는_인식하지_않는다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "bearer token123")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        void 대문자_BEARER는_인식하지_않는다() {
            // given
            ServerHttpRequest request = MockServerHttpRequest
                .get("/test")
                .header(HttpHeaders.AUTHORIZATION, "BEARER token123")
                .build();

            // when
            Optional<Token> result = bearerTokenExtractor.extract(request);

            // then
            assertThat(result).isEmpty();
        }
    }
}
