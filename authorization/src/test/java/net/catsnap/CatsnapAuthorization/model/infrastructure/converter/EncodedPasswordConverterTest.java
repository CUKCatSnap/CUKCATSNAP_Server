package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void String을_EncodedPassword_값_객체로_변환한다() {
        // given
        String dbData = "$2a$10$xyz7890abcdefghijklmnopqrstuvwxyz";

        // when
        EncodedPassword result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals(dbData, result.getValue());
    }
}
