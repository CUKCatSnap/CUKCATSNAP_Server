package net.catsnap.CatsnapGateway.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TokenClaimsTest {

    @Nested
    class 유효한_정보를_인증_정보를_전달할_때 {

        @Test
        void 유효한_클레임_정보로_생성에_성공한다() {
            // given
            Long userId = 1L;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;

            // when
            TokenClaims tokenClaims = new TokenClaims(userId, authority);

            // then
            assertThat(tokenClaims.userId()).isEqualTo(userId);
            assertThat(tokenClaims.authority()).isEqualTo(authority);
        }

        @Test
        void 모델_권한으로_생성에_성공한다() {
            // given
            Long userId = 2L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;

            // when
            TokenClaims tokenClaims = new TokenClaims(userId, authority);

            // then
            assertThat(tokenClaims.userId()).isEqualTo(userId);
            assertThat(tokenClaims.authority()).isEqualTo(authority);
        }

        @Test
        void userId가_null이면_예외가_발생한다() {
            // given
            Long nullUserId = null;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;

            // when & then
            assertThatThrownBy(() -> new TokenClaims(nullUserId, authority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserId cannot be null");
        }

        @Test
        void authority가_null이면_예외가_발생한다() {
            // given
            Long userId = 1L;
            CatsnapAuthority nullAuthority = null;

            // when & then
            assertThatThrownBy(() -> new TokenClaims(userId, nullAuthority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authority cannot be null");
        }
    }

    @Nested
    class TokenClaims_동등성_비교_시 {

        @Test
        void 같은_값을_가진_TokenClaims는_동등하다() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);
            TokenClaims claims2 = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(claims1).isEqualTo(claims2);
            assertThat(claims1.hashCode()).isEqualTo(claims2.hashCode());
        }

        @Test
        void 다른_userId를_가진_TokenClaims는_동등하지_않다() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);
            TokenClaims claims2 = new TokenClaims(2L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(claims1).isNotEqualTo(claims2);
        }

        @Test
        void 다른_authority를_가진_TokenClaims는_동등하지_않다() {
            // given
            TokenClaims claims1 = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);
            TokenClaims claims2 = new TokenClaims(1L, CatsnapAuthority.MODEL);

            // when & then
            assertThat(claims1).isNotEqualTo(claims2);
        }
    }

    @Nested
    class 익명_사용자_관련_메서드 {

        @Test
        void anonymous_메서드로_익명_사용자_TokenClaims를_생성한다() {
            // when
            TokenClaims anonymous = TokenClaims.anonymous();

            // then
            assertThat(anonymous.userId()).isEqualTo(-1L);
            assertThat(anonymous.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
        }

        @Test
        void 익명_사용자는_isAnonymous_true를_반환한다() {
            // given
            TokenClaims anonymous = TokenClaims.anonymous();

            // when & then
            assertThat(anonymous.isAnonymous()).isTrue();
        }

        @Test
        void 익명_사용자는_isAuthenticated_false를_반환한다() {
            // given
            TokenClaims anonymous = TokenClaims.anonymous();

            // when & then
            assertThat(anonymous.isAuthenticated()).isFalse();
        }

        @Test
        void 인증된_사용자는_isAnonymous_false를_반환한다() {
            // given
            TokenClaims authenticated = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(authenticated.isAnonymous()).isFalse();
        }

        @Test
        void 인증된_사용자는_isAuthenticated_true를_반환한다() {
            // given
            TokenClaims authenticated = new TokenClaims(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(authenticated.isAuthenticated()).isTrue();
        }
    }
}
