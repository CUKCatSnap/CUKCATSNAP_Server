package net.catsnap.shared.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CatsnapAuthorityTest {

    @Test
    void getAuthorityName_메서드는_권한_이름의_소문자를_반환해야_합니다() {
        for (CatsnapAuthority authority : CatsnapAuthority.values()) {
            assertEquals(authority.name().toLowerCase(), authority.getAuthorityName());
        }
    }

    @Nested
    class 문자열에서_권한_추출_함수_테스트 {

        @Test
        void model_문자열에서_권한을_추출한다() {
            //given
            String model = "model";
            String modelUpper = "MODEL";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(model);
            Optional<CatsnapAuthority> authorityUpper = CatsnapAuthority.findAuthorityByName(
                modelUpper);

            //then
            Assertions.assertEquals(CatsnapAuthority.MODEL, authority.get());
            Assertions.assertEquals(CatsnapAuthority.MODEL, authorityUpper.get());
        }

        @Test
        void photographer_문자열에서_권한을_추출한다() {
            //given
            String photographer = "photographer";
            String photographerUpper = "PHOTOGRAPHER";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(
                photographer);
            Optional<CatsnapAuthority> authorityUpper = CatsnapAuthority.findAuthorityByName(
                photographerUpper);

            //then
            Assertions.assertEquals(CatsnapAuthority.PHOTOGRAPHER, authority.get());
            Assertions.assertEquals(CatsnapAuthority.PHOTOGRAPHER, authorityUpper.get());
        }

        @Test
        void admin_문자열에서_권한을_추출한다() {
            //given
            String admin = "admin";
            String adminUpper = "ADMIN";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(admin);
            Optional<CatsnapAuthority> authorityUpper = CatsnapAuthority.findAuthorityByName(
                adminUpper);

            //then
            Assertions.assertEquals(CatsnapAuthority.ADMIN, authority.get());
            Assertions.assertEquals(CatsnapAuthority.ADMIN, authorityUpper.get());
        }

        @Test
        void anonymous_문자열에서_권한을_추출한다() {
            //given
            String anonymous = "anonymous";
            String anonymousUpper = "ANONYMOUS";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(anonymous);
            Optional<CatsnapAuthority> authorityUpper = CatsnapAuthority.findAuthorityByName(
                anonymousUpper);

            //then
            Assertions.assertEquals(CatsnapAuthority.ANONYMOUS, authority.get());
            Assertions.assertEquals(CatsnapAuthority.ANONYMOUS, authorityUpper.get());
        }

        @Test
        void 빈_문자열이_들어오면_빈_Optional을_반환한다() {
            //given
            String empty = "";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(empty);

            //then
            assertTrue(authority.isEmpty());
        }

        @Test
        void null이_들어오면_빈_Optional을_반환한다() {
            //given
            String nullString = null;

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(nullString);

            //then
            assertTrue(authority.isEmpty());
        }

        @Test
        void 잘못된_문자열이_들어오면_빈_Optional을_반환한다() {
            //given
            String invalid = "invalid";

            //when
            Optional<CatsnapAuthority> authority = CatsnapAuthority.findAuthorityByName(invalid);

            //then
            assertTrue(authority.isEmpty());
        }
    }
}
