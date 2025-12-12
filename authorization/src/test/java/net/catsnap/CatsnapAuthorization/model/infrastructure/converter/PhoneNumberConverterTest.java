package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.catsnap.CatsnapAuthorization.model.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PhoneNumberConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhoneNumberConverterTest {

    private PhoneNumberConverter converter;

    @BeforeEach
    void setUp() {
        converter = new PhoneNumberConverter();
    }

    @Test
    void PhoneNumber_값_객체를_String으로_변환한다() {
        // given
        PhoneNumber phoneNumber = new PhoneNumber("010-1234-5678");

        // when
        String result = converter.convertToDatabaseColumn(phoneNumber);

        // then
        assertEquals("010-1234-5678", result);
    }

    @Test
    void null_PhoneNumber를_null_String으로_변환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertNull(result);
    }

    @Test
    void String을_PhoneNumber_값_객체로_변환한다() {
        // given
        String dbData = "010-9876-5432";

        // when
        PhoneNumber result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("010-9876-5432", result.getValue());
    }

    @Test
    void 하이픈_없는_전화번호도_변환한다() {
        // given
        String dbData = "01012345678";

        // when
        PhoneNumber result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("01012345678", result.getValue());
    }

    @Test
    void null_String을_null_PhoneNumber로_변환한다() {
        // when
        PhoneNumber result = converter.convertToEntityAttribute(null);

        // then
        assertNull(result);
    }

    @Test
    void 양방향_변환이_정상적으로_동작한다() {
        // given
        PhoneNumber original = new PhoneNumber("010-5555-6666");

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        PhoneNumber converted = converter.convertToEntityAttribute(dbData);

        // then
        assertEquals(original, converted);
        assertEquals(original.getValue(), converted.getValue());
    }
}
