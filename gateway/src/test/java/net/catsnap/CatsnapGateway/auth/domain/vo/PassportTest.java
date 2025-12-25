package net.catsnap.CatsnapGateway.auth.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.shared.auth.CatsnapAuthority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Passport 테스트")
class PassportTest {

    @Nested
    @DisplayName("Passport 생성 시")
    class CreatePassport {

        @Test
        @DisplayName("유효한 인증 정보로 생성에 성공한다")
        void createWithValidInformation() {
            // given
            Long userId = 1L;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;

            // when
            Passport passport = new Passport(userId, authority);

            // then
            assertThat(passport.userId()).isEqualTo(userId);
            assertThat(passport.authority()).isEqualTo(authority);
        }

        @Test
        @DisplayName("MODEL 권한으로 생성에 성공한다")
        void createWithModelAuthority() {
            // given
            Long userId = 2L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;

            // when
            Passport passport = new Passport(userId, authority);

            // then
            assertThat(passport.userId()).isEqualTo(userId);
            assertThat(passport.authority()).isEqualTo(authority);
        }

        @Test
        @DisplayName("userId가 null이면 예외가 발생한다")
        void createWithNullUserId() {
            // given
            Long nullUserId = null;
            CatsnapAuthority authority = CatsnapAuthority.PHOTOGRAPHER;

            // when & then
            assertThatThrownBy(() -> new Passport(nullUserId, authority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("UserId cannot be null");
        }

        @Test
        @DisplayName("authority가 null이면 예외가 발생한다")
        void createWithNullAuthority() {
            // given
            Long userId = 1L;
            CatsnapAuthority nullAuthority = null;

            // when & then
            assertThatThrownBy(() -> new Passport(userId, nullAuthority))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Authority cannot be null");
        }
    }

    @Nested
    @DisplayName("익명 사용자 Passport")
    class AnonymousPassport {

        @Test
        @DisplayName("createAnonymous로 익명 사용자 Passport를 생성한다")
        void createAnonymousPassport() {
            // when
            Passport anonymousPassport = Passport.createAnonymous();

            // then
            assertThat(anonymousPassport.userId()).isEqualTo(-1L);
            assertThat(anonymousPassport.authority()).isEqualTo(CatsnapAuthority.ANONYMOUS);
        }

        @Test
        @DisplayName("익명 사용자는 isAnonymous()가 true를 반환한다")
        void anonymousUserIsAnonymous() {
            // given
            Passport anonymousPassport = Passport.createAnonymous();

            // when
            boolean isAnonymous = anonymousPassport.isAnonymous();

            // then
            assertThat(isAnonymous).isTrue();
        }

        @Test
        @DisplayName("익명 사용자는 isAuthenticated()가 false를 반환한다")
        void anonymousUserIsNotAuthenticated() {
            // given
            Passport anonymousPassport = Passport.createAnonymous();

            // when
            boolean isAuthenticated = anonymousPassport.isAuthenticated();

            // then
            assertThat(isAuthenticated).isFalse();
        }
    }

    @Nested
    @DisplayName("인증된 사용자 Passport")
    class AuthenticatedPassport {

        @Test
        @DisplayName("인증된 사용자는 isAnonymous()가 false를 반환한다")
        void authenticatedUserIsNotAnonymous() {
            // given
            Passport passport = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when
            boolean isAnonymous = passport.isAnonymous();

            // then
            assertThat(isAnonymous).isFalse();
        }

        @Test
        @DisplayName("인증된 사용자는 isAuthenticated()가 true를 반환한다")
        void authenticatedUserIsAuthenticated() {
            // given
            Passport passport = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when
            boolean isAuthenticated = passport.isAuthenticated();

            // then
            assertThat(isAuthenticated).isTrue();
        }
    }

    @Nested
    @DisplayName("Passport 동등성 비교 시")
    class PassportEquality {

        @Test
        @DisplayName("같은 값을 가진 Passport는 동등하다")
        void equalPassportsWithSameValue() {
            // given
            Passport passport1 = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);
            Passport passport2 = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(passport1).isEqualTo(passport2);
            assertThat(passport1.hashCode()).isEqualTo(passport2.hashCode());
        }

        @Test
        @DisplayName("다른 userId를 가진 Passport는 동등하지 않다")
        void notEqualPassportsWithDifferentUserId() {
            // given
            Passport passport1 = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);
            Passport passport2 = new Passport(2L, CatsnapAuthority.PHOTOGRAPHER);

            // when & then
            assertThat(passport1).isNotEqualTo(passport2);
        }

        @Test
        @DisplayName("다른 authority를 가진 Passport는 동등하지 않다")
        void notEqualPassportsWithDifferentAuthority() {
            // given
            Passport passport1 = new Passport(1L, CatsnapAuthority.PHOTOGRAPHER);
            Passport passport2 = new Passport(1L, CatsnapAuthority.MODEL);

            // when & then
            assertThat(passport1).isNotEqualTo(passport2);
        }
    }
}
