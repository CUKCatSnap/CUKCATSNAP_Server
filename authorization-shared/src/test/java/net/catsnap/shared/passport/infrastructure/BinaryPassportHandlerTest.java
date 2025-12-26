package net.catsnap.shared.passport.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.crypto.SecretKey;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;
import net.catsnap.shared.passport.domain.exception.InvalidPassportException;
import net.catsnap.shared.passport.domain.exception.PassportParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BinaryPassportHandlerTest {

    private BinaryPassportHandler handler;
    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        // 32바이트 테스트 시크릿 키 생성
        String keyString = "test-secret-key-32-bytes-long!!";
        handler = new BinaryPassportHandler(keyString);
    }

    @Nested
    class 생성자_테스트 {

        @Test
        void null_시크릿_키로_생성하면_예외가_발생한다() {
            //given & when & then
            assertThrows(IllegalArgumentException.class, () -> {
                new BinaryPassportHandler(null);
            });
        }
    }

    @Nested
    class 서명_테스트 {

        @Test
        void 정상적인_Passport를_서명한다() {
            //given
            Instant now = Instant.now();
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 123L, CatsnapAuthority.MODEL, now, exp);

            //when
            String signed = handler.sign(passport);

            //then
            assertTrue(signed != null && !signed.isBlank());
        }

        @Test
        void null_Passport를_서명하면_예외가_발생한다() {
            //given
            Passport nullPassport = null;

            //when & then
            assertThrows(IllegalArgumentException.class, () -> {
                handler.sign(nullPassport);
            });
        }

        @Test
        void 서명된_문자열은_Base64_형식이다() {
            //given
            Instant now = Instant.now();
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 123L, CatsnapAuthority.PHOTOGRAPHER, now,
                exp);

            //when
            String signed = handler.sign(passport);

            //then
            // Base64 문자열은 A-Z, a-z, 0-9, +, /, = 로만 구성됨
            assertTrue(signed.matches("^[A-Za-z0-9+/=]+$"));
        }

        @Test
        void 이전_날짜로_설정된_만기의_Passport는_예외가_발생한다() {
            //given
            Instant past = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES); // 5분 전 만료
            Passport expiredPassport = new Passport((byte) 1, 999L, CatsnapAuthority.MODEL, past,
                exp);

            //when & then
            assertThrows(ExpiredPassportException.class, () -> handler.sign(expiredPassport));
        }
    }

    @Nested
    class 파싱_테스트 {

        @Test
        void 정상적인_서명된_Passport를_파싱한다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS); // 초 단위로 절삭
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport original = new Passport((byte) 1, 456L, CatsnapAuthority.ADMIN, now, exp);
            String signed = handler.sign(original);

            //when
            Passport result = handler.parse(signed);

            //then
            assertNotNull(result);
            assertEquals(original.version(), result.version());
            assertEquals(original.userId(), result.userId());
            assertEquals(original.authority(), result.authority());
            assertEquals(original.iat(), result.iat());
            assertEquals(original.exp(), result.exp());
        }

        @Test
        void null_입력_시_예외가_발생한다() {
            //given
            String nullInput = null;

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(nullInput));
        }

        @Test
        void 빈_문자열_입력_시_예외가_발생한다() {
            //given
            String emptyInput = "";

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(emptyInput));
        }

        @Test
        void 공백_문자열_입력_시_예외가_발생한다() {
            //given
            String blankInput = "   ";

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(blankInput));
        }

        @Test
        void 잘못된_Base64_형식_시_예외가_발생한다() {
            //given
            String invalidBase64 = "this-is-not-base64!@#$";

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(invalidBase64));
        }

        @Test
        void 잘못된_길이의_데이터_시_예외가_발생한다() {
            //given
            String shortData = "YWJj"; // "abc" in base64 (3 bytes, expected 58)

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(shortData));
        }

        @Test
        void 잘못된_서명_시_예외가_발생한다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 789L, CatsnapAuthority.ANONYMOUS, now, exp);
            String signed = handler.sign(passport);

            // 서명 부분 변조 (마지막 문자 변경)
            String tampered = signed.substring(0, signed.length() - 1) + "X";

            //when & then
            assertThrows(PassportParsingException.class, () -> handler.parse(tampered));
        }
    }

    @Nested
    class 왕복_변환_테스트 {

        @Test
        void 모든_권한에_대해_서명_후_파싱이_성공한다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            Instant exp = now.plus(5, ChronoUnit.MINUTES);

            //when & then
            for (CatsnapAuthority authority : CatsnapAuthority.values()) {
                Passport original = new Passport((byte) 1, 123L, authority, now, exp);
                String signed = handler.sign(original);
                Passport parsed = handler.parse(signed);

                assertNotNull(parsed);
                assertEquals(original.authority(), parsed.authority());
            }
        }

        @Test
        void 다양한_userId에_대해_왕복_변환이_성공한다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            long[] userIds = {1L, 100L, 999999L, Long.MAX_VALUE};

            //when & then
            for (long userId : userIds) {
                Passport original = new Passport((byte) 1, userId, CatsnapAuthority.MODEL, now,
                    exp);
                String signed = handler.sign(original);
                Passport parsed = handler.parse(signed);

                assertNotNull(parsed);
                assertEquals(userId, parsed.userId());
            }
        }

        @Test
        void 서로_다른_시크릿_키로는_파싱할_수_없다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 123L, CatsnapAuthority.PHOTOGRAPHER, now,
                exp);

            String signed = handler.sign(passport);

            // 다른 시크릿 키로 새로운 핸들러 생성
            String differentKeyString = "different-key-32-bytes-long!!!";
            BinaryPassportHandler differentHandler = new BinaryPassportHandler(differentKeyString);

            //when & then
            assertThrows(InvalidPassportException.class, () -> differentHandler.parse(signed));
        }
    }
}
