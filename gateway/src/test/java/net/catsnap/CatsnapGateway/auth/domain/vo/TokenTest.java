package net.catsnap.CatsnapGateway.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Token 테스트")
class TokenTest {

    @Nested
    @DisplayName("Token 생성 시")
    class CreateToken {

        @Test
        @DisplayName("유효한 토큰 문자열로 생성에 성공한다")
        void createWithValidToken() {
            // given
            String validTokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";

            // when
            Token token = new Token(validTokenValue);

            // then
            assertThat(token.value()).isEqualTo(validTokenValue);
        }

        @Test
        @DisplayName("null 토큰으로 생성 시 예외가 발생한다")
        void createWithNullToken() {
            // given
            String nullToken = null;

            // when & then
            assertThatThrownBy(() -> new Token(nullToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }

        @Test
        @DisplayName("빈 문자열 토큰으로 생성 시 예외가 발생한다")
        void createWithEmptyToken() {
            // given
            String emptyToken = "";

            // when & then
            assertThatThrownBy(() -> new Token(emptyToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }

        @Test
        @DisplayName("공백만 있는 토큰으로 생성 시 예외가 발생한다")
        void createWithBlankToken() {
            // given
            String blankToken = "   ";

            // when & then
            assertThatThrownBy(() -> new Token(blankToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("Token 동등성 비교 시")
    class TokenEquality {

        @Test
        @DisplayName("같은 값을 가진 Token은 동등하다")
        void equalTokensWithSameValue() {
            // given
            String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            Token token1 = new Token(tokenValue);
            Token token2 = new Token(tokenValue);

            // when & then
            assertThat(token1).isEqualTo(token2);
            assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
        }

        @Test
        @DisplayName("다른 값을 가진 Token은 동등하지 않다")
        void notEqualTokensWithDifferentValue() {
            // given
            Token token1 = new Token("token1");
            Token token2 = new Token("token2");

            // when & then
            assertThat(token1).isNotEqualTo(token2);
        }
    }
}
