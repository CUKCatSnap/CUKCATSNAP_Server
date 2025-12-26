package net.catsnap.shared.passport.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PassportTest {

    @Nested
    class 생성_테스트 {

        @Test
        void 정상적인_Passport를_생성한다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);

            //when
            Passport passport = new Passport(version, userId, authority, iat, exp);

            //then
            assertEquals(version, passport.version());
            assertEquals(userId, passport.userId());
            assertEquals(authority, passport.authority());
            assertEquals(iat, passport.iat());
            assertEquals(exp, passport.exp());
        }

        @Test
        void version이_1_미만이면_예외가_발생한다() {
            //given
            byte invalidVersion = 0;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);

            //when & then
            assertThrows(IllegalArgumentException.class,
                () -> new Passport(invalidVersion, userId, authority, iat, exp));
        }

        @Test
        void authority가_null이면_예외가_발생한다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority nullAuthority = null;
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);

            //when & then
            assertThrows(IllegalArgumentException.class,
                () -> new Passport(version, userId, nullAuthority, iat, exp));
        }

        @Test
        void iat가_null이면_예외가_발생한다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant nullIat = null;
            Instant exp = Instant.now().plus(5, ChronoUnit.MINUTES);

            //when & then
            assertThrows(IllegalArgumentException.class,
                () -> new Passport(version, userId, authority, nullIat, exp));
        }

        @Test
        void exp가_null이면_예외가_발생한다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant iat = Instant.now();
            Instant nullExp = null;

            //when & then
            assertThrows(IllegalArgumentException.class,
                () -> new Passport(version, userId, authority, iat, nullExp));
        }

        @Test
        void iat가_exp보다_이후이면_예외가_발생한다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant exp = Instant.now();
            Instant iat = exp.plus(5, ChronoUnit.MINUTES); // iat가 exp보다 나중

            //when & then
            assertThrows(IllegalArgumentException.class,
                () -> new Passport(version, userId, authority, iat, exp));
        }

        @Test
        void iat와_exp가_같으면_정상적으로_생성된다() {
            //given
            byte version = 1;
            long userId = 123L;
            CatsnapAuthority authority = CatsnapAuthority.MODEL;
            Instant sameTime = Instant.now();

            //when
            Passport passport = new Passport(version, userId, authority, sameTime, sameTime);

            //then
            assertEquals(sameTime, passport.iat());
            assertEquals(sameTime, passport.exp());
        }
    }

    @Nested
    class userId_접근_테스트 {

        @Test
        void 만료되지_않은_Passport에서_userId를_조회한다() {
            //given
            long expectedUserId = 456L;
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, expectedUserId, CatsnapAuthority.PHOTOGRAPHER,
                iat, exp);

            //when
            long actualUserId = passport.userId();

            //then
            assertEquals(expectedUserId, actualUserId);
        }

        @Test
        void 만료된_Passport에서_userId_조회_시_예외가_발생한다() {
            //given
            Instant iat = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 789L, CatsnapAuthority.ADMIN, iat, exp);

            //when & then
            assertThrows(ExpiredPassportException.class, passport::userId);
        }
    }

    @Nested
    class authority_접근_테스트 {

        @Test
        void 만료되지_않은_Passport에서_authority를_조회한다() {
            //given
            CatsnapAuthority expectedAuthority = CatsnapAuthority.PHOTOGRAPHER;
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 123L, expectedAuthority, iat, exp);

            //when
            CatsnapAuthority actualAuthority = passport.authority();

            //then
            assertEquals(expectedAuthority, actualAuthority);
        }

        @Test
        void 만료된_Passport에서_authority_조회_시_예외가_발생한다() {
            //given
            Instant iat = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 999L, CatsnapAuthority.MODEL, iat, exp);

            //when & then
            assertThrows(ExpiredPassportException.class, passport::authority);
        }

        @Test
        void 모든_권한_타입에_대해_정상_조회가_가능하다() {
            //given
            Instant iat = Instant.now();
            Instant exp = iat.plus(5, ChronoUnit.MINUTES);

            //when & then
            for (CatsnapAuthority authority : CatsnapAuthority.values()) {
                Passport passport = new Passport((byte) 1, 123L, authority, iat, exp);
                assertEquals(authority, passport.authority());
            }
        }
    }

    @Nested
    class version과_시간_접근_테스트 {

        @Test
        void version은_만료_여부와_관계없이_항상_조회_가능하다() {
            //given
            byte expectedVersion = 2;
            Instant iat = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES); // 만료됨
            Passport passport = new Passport(expectedVersion, 123L, CatsnapAuthority.MODEL, iat,
                exp);

            //when
            byte actualVersion = passport.version();

            //then
            assertEquals(expectedVersion, actualVersion);
        }

        @Test
        void iat는_만료_여부와_관계없이_항상_조회_가능하다() {
            //given
            Instant expectedIat = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES); // 만료됨
            Passport passport = new Passport((byte) 1, 123L, CatsnapAuthority.MODEL, expectedIat,
                exp);

            //when
            Instant actualIat = passport.iat();

            //then
            assertEquals(expectedIat, actualIat);
        }

        @Test
        void exp는_만료_여부와_관계없이_항상_조회_가능하다() {
            //given
            Instant iat = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant expectedExp = Instant.now().minus(5, ChronoUnit.MINUTES); // 만료됨
            Passport passport = new Passport((byte) 1, 123L, CatsnapAuthority.MODEL, iat,
                expectedExp);

            //when
            Instant actualExp = passport.exp();

            //then
            assertEquals(expectedExp, actualExp);
        }
    }
}