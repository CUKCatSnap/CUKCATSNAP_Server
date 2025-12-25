package net.catsnap.CatsnapGateway.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TokenClaims 테스트")
class TokenClaimsTest {

    @Nested
    @DisplayName("TokenClaims 생성 시")
    class CreateTokenClaims {

        @Test
        @DisplayName("유효한 클레임 정보로 생성에 성공한다")
        void createWithValidClaims() {
            // given
            Long userId = 1L;
            String authority = "PHOTOGRAPHER";

            // when
            TokenClaims tokenClaims = new TokenClaims(userId, authority);

            // then
            assertThat(tokenClaims.userId()).isEqualTo(userId);
            assertThat(tokenClaims.authority()).isEqualTo(authority);
        }

        @Test
        @DisplayName("MODEL 권한으로 생성에 성공한다")
        void createWithModelAuthority() {
            // given
            Long userId = 2L;
            String authority = "MODEL";

            // when
            TokenClaims tokenClaims = new TokenClaims(userId, authority);

            // then
            assertThat(tokenClaims.userId()).isEqualTo(userId);
            assertThat(tokenClaims.authority()).isEqualTo(authority);
        }

        @Test
        @DisplayName("userId가 null이면 예외가 발생한다")
        void createWithNullUserId() {
            // given
            Long nullUserId = null;
            String authority = "PHOTOGRAPHER";

            // when & then
            assertThatThrownBy(() -> new TokenClaims(nullUserId, authority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserId cannot be null");
        }

        @Test
        @DisplayName("authority가 null이면 예외가 발생한다")
        void createWithNullAuthority() {
            // given
            Long userId = 1L;
            String nullAuthority = null;

            // when & then
            assertThatThrownBy(() -> new TokenClaims(userId, nullAuthority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authority cannot be null or empty");
        }

        @Test
        @DisplayName("authority가 빈 문자열이면 예외가 발생한다")
        void createWithEmptyAuthority() {
            // given
            Long userId = 1L;
            String emptyAuthority = "";

            // when & then
            assertThatThrownBy(() -> new TokenClaims(userId, emptyAuthority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authority cannot be null or empty");
        }

        @Test
        @DisplayName("authority가 공백만 있으면 예외가 발생한다")
        void createWithBlankAuthority() {
            // given
            Long userId = 1L;
            String blankAuthority = "   ";

            // when & then
            assertThatThrownBy(() -> new TokenClaims(userId, blankAuthority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authority cannot be null or empty");
        }
    }

    @Nested
    @DisplayName("TokenClaims 동등성 비교 시")
    class TokenClaimsEquality {

        @Test
        @DisplayName("같은 값을 가진 TokenClaims는 동등하다")
        void equalTokenClaimsWithSameValue() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, "PHOTOGRAPHER");
            TokenClaims claims2 = new TokenClaims(1L, "PHOTOGRAPHER");

            // when & then
            assertThat(claims1).isEqualTo(claims2);
            assertThat(claims1.hashCode()).isEqualTo(claims2.hashCode());
        }

        @Test
        @DisplayName("다른 userId를 가진 TokenClaims는 동등하지 않다")
        void notEqualTokenClaimsWithDifferentUserId() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, "PHOTOGRAPHER");
            TokenClaims claims2 = new TokenClaims(2L, "PHOTOGRAPHER");

            // when & then
            assertThat(claims1).isNotEqualTo(claims2);
        }

        @Test
        @DisplayName("다른 authority를 가진 TokenClaims는 동등하지 않다")
        void notEqualTokenClaimsWithDifferentAuthority() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, "PHOTOGRAPHER");
            TokenClaims claims2 = new TokenClaims(1L, "MODEL");

            // when & then
            assertThat(claims1).isNotEqualTo(claims2);
        }
    }
}
