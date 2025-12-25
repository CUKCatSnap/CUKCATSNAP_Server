package net.catsnap.CatsnapGateway.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import net.catsnap.CatsnapGateway.auth.domain.vo.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

@DisplayName("BearerTokenExtractor 테스트")
class BearerTokenExtractorTest {

    private BearerTokenExtractor bearerTokenExtractor;

    @BeforeEach
    void setUp() {
        bearerTokenExtractor = new BearerTokenExtractor();
    }

    @Nested
    @DisplayName("유효한 Bearer 토큰 추출 시")
    class ExtractValidBearerToken {

        @Test
        @DisplayName("Authorization 헤더에서 Bearer 토큰을 추출한다")
        void extractBearerToken() {
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
        @DisplayName("긴 Bearer 토큰을 추출한다")
        void extractLongBearerToken() {
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
    @DisplayName("Authorization 헤더가 없는 경우")
    class NoAuthorizationHeader {

        @Test
        @DisplayName("빈 Optional을 반환한다")
        void returnEmptyOptional() {
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
    @DisplayName("Bearer 접두사가 없는 경우")
    class NoBearerPrefix {

        @Test
        @DisplayName("토큰만 있는 경우 빈 Optional을 반환한다")
        void returnEmptyOptionalWhenTokenOnly() {
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
        @DisplayName("다른 인증 방식(Basic)인 경우 빈 Optional을 반환한다")
        void returnEmptyOptionalWhenBasicAuth() {
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
    @DisplayName("Bearer 뒤에 토큰이 없는 경우")
    class BearerWithoutToken {

        @Test
        @DisplayName("Bearer만 있는 경우 빈 Optional을 반환한다")
        void returnEmptyOptionalWhenBearerOnly() {
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
        @DisplayName("Bearer 뒤에 공백만 있는 경우 빈 Optional을 반환한다")
        void returnEmptyOptionalWhenBearerWithSpaces() {
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
    @DisplayName("대소문자 구분")
    class CaseSensitivity {

        @Test
        @DisplayName("소문자 bearer는 인식하지 않는다")
        void notRecognizeLowercaseBearer() {
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
        @DisplayName("BEARER(대문자)는 인식하지 않는다")
        void notRecognizeUppercaseBearer() {
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
