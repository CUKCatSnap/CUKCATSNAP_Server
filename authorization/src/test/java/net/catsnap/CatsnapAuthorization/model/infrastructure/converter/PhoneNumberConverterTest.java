package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void String을_PhoneNumber_값_객체로_변환한다() {
        // given
        String dbData = "010-9876-5432";

        // when
        PhoneNumber result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("010-9876-5432", result.getValue());
    }
}
