package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.catsnap.CatsnapAuthorization.model.domain.vo.EncodedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("EncodedPasswordConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EncodedPasswordConverterTest {

    private EncodedPasswordConverter converter;

    @BeforeEach
    void setUp() {
        converter = new EncodedPasswordConverter();
    }

    @Test
    void EncodedPassword_값_객체를_String으로_변환한다() {
        // given
        String encodedValue = "$2a$10$abcdefghijklmnopqrstuvwxyz1234567890";
        EncodedPassword encodedPassword = new EncodedPassword(encodedValue);

        // when
        String result = converter.convertToDatabaseColumn(encodedPassword);

        // then
        assertEquals(encodedValue, result);
    }

    @Test
    void null_EncodedPassword를_null_String으로_변환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertNull(result);
    }

    @Test
    void String을_EncodedPassword_값_객체로_변환한다() {
        // given
        String dbData = "$2a$10$xyz7890abcdefghijklmnopqrstuvwxyz";

        // when
        EncodedPassword result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals(dbData, result.getValue());
    }

    @Test
    void null_String을_null_EncodedPassword로_변환한다() {
        // when
        EncodedPassword result = converter.convertToEntityAttribute(null);

        // then
        assertNull(result);
    }

    @Test
    void 양방향_변환이_정상적으로_동작한다() {
        // given
        String encodedValue = "$2a$10$testEncodedPasswordValue123456";
        EncodedPassword original = new EncodedPassword(encodedValue);

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        EncodedPassword converted = converter.convertToEntityAttribute(dbData);

        // then
        assertEquals(original, converted);
        assertEquals(original.getValue(), converted.getValue());
    }

    @Test
    @DisplayName("실제 BCrypt 형식의 암호화된 비밀번호를 변환한다")
    void 실제_BCrypt_형식의_암호화된_비밀번호를_변환한다() {
        // given
        // 실제 BCrypt로 "password123"을 암호화한 값 예시
        String realBCryptHash = "$2a$10$N9qo8uLOickgx2ZMRZoMye/JDMaZqj8QJZ.YFk3BdG2LVQD1cQVOy";
        EncodedPassword encodedPassword = new EncodedPassword(realBCryptHash);

        // when
        String dbData = converter.convertToDatabaseColumn(encodedPassword);
        EncodedPassword converted = converter.convertToEntityAttribute(dbData);

        // then
        assertEquals(realBCryptHash, dbData);
        assertEquals(realBCryptHash, converted.getValue());
    }
}
