package net.catsnap.CatsnapGateway.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TokenTest {

    @Nested
    class 토큰_생성_시 {

        @Test
        void 유효한_문자열로_토큰_생성에_성공한다() {
            // given
            String validTokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";

            // when
            Token token = new Token(validTokenValue);

            // then
            assertThat(token.value()).isEqualTo(validTokenValue);
        }

        @Test
        void null_문자열로_생성시_예외가_발생한다() {
            // given
            String nullToken = null;

            // when & then
            assertThatThrownBy(() -> new Token(nullToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }

        @Test
        void 빈_문자열_토큰으로_생성시_예외가_발생한다() {
            // given
            String emptyToken = "";

            // when & then
            assertThatThrownBy(() -> new Token(emptyToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }

        @Test
        void 공백만_있는_토큰으로_생성시_예외가_발생한다() {
            // given
            String blankToken = "   ";

            // when & then
            assertThatThrownBy(() -> new Token(blankToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Token value cannot be null or empty");
        }
    }

    @Nested
    class 동등성_비교_시 {

        @Test
        void 같은_값을_가진_Token은_동등하다() {
            // given
            String tokenValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            Token token1 = new Token(tokenValue);
            Token token2 = new Token(tokenValue);

            // when & then
            assertThat(token1).isEqualTo(token2);
            assertThat(token1.hashCode()).isEqualTo(token2.hashCode());
        }

        @Test
        void 다른_값을_가진_Token은_동등하지_않다() {
            // given
            Token token1 = new Token("token1");
            Token token2 = new Token("token2");

            // when & then
            assertThat(token1).isNotEqualTo(token2);
        }
    }
}
