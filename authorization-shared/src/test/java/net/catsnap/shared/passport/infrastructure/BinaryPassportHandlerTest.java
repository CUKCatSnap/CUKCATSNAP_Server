package net.catsnap.shared.passport.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
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
        secretKey = new SecretKeySpec(keyString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        handler = new BinaryPassportHandler(secretKey);
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
            Optional<Passport> result = handler.parse(signed);

            //then
            assertTrue(result.isPresent());
            assertEquals(original.version(), result.get().version());
            assertEquals(original.userId(), result.get().userId());
            assertEquals(original.authority(), result.get().authority());
            assertEquals(original.iat(), result.get().iat());
            assertEquals(original.exp(), result.get().exp());
        }

        @Test
        void null_입력_시_빈_Optional을_반환한다() {
            //given
            String nullInput = null;

            //when
            Optional<Passport> result = handler.parse(nullInput);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 빈_문자열_입력_시_빈_Optional을_반환한다() {
            //given
            String emptyInput = "";

            //when
            Optional<Passport> result = handler.parse(emptyInput);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 공백_문자열_입력_시_빈_Optional을_반환한다() {
            //given
            String blankInput = "   ";

            //when
            Optional<Passport> result = handler.parse(blankInput);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 잘못된_Base64_형식_시_빈_Optional을_반환한다() {
            //given
            String invalidBase64 = "this-is-not-base64!@#$";

            //when
            Optional<Passport> result = handler.parse(invalidBase64);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 잘못된_길이의_데이터_시_빈_Optional을_반환한다() {
            //given
            String shortData = "YWJj"; // "abc" in base64 (3 bytes, expected 58)

            //when
            Optional<Passport> result = handler.parse(shortData);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 잘못된_서명_시_빈_Optional을_반환한다() {
            //given
            Instant now = Instant.now().truncatedTo(ChronoUnit.SECONDS);
            Instant exp = now.plus(5, ChronoUnit.MINUTES);
            Passport passport = new Passport((byte) 1, 789L, CatsnapAuthority.ANONYMOUS, now, exp);
            String signed = handler.sign(passport);

            // 서명 부분 변조 (마지막 문자 변경)
            String tampered = signed.substring(0, signed.length() - 1) + "X";

            //when
            Optional<Passport> result = handler.parse(tampered);

            //then
            assertTrue(result.isEmpty());
        }

        @Test
        void 만료된_Passport는_빈_Optional을_반환한다() {
            //given
            Instant past = Instant.now().minus(10, ChronoUnit.MINUTES);
            Instant exp = Instant.now().minus(5, ChronoUnit.MINUTES); // 5분 전 만료
            Passport expiredPassport = new Passport((byte) 1, 999L, CatsnapAuthority.MODEL, past,
                exp);
            String signed = handler.sign(expiredPassport);

            //when
            Optional<Passport> result = handler.parse(signed);

            //then
            assertTrue(result.isEmpty());
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
                Optional<Passport> parsed = handler.parse(signed);

                assertTrue(parsed.isPresent());
                assertEquals(original.authority(), parsed.get().authority());
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
                Optional<Passport> parsed = handler.parse(signed);

                assertTrue(parsed.isPresent());
                assertEquals(userId, parsed.get().userId());
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
            SecretKey differentKey = new SecretKeySpec(
                differentKeyString.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            BinaryPassportHandler differentHandler = new BinaryPassportHandler(differentKey);

            //when
            Optional<Passport> result = differentHandler.parse(signed);

            //then
            assertTrue(result.isEmpty());
        }
    }
}
