package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("IdentifierConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class IdentifierConverterTest {

    private IdentifierConverter converter;

    @BeforeEach
    void setUp() {
        converter = new IdentifierConverter();
    }

    @Test
    void Identifier_값_객체를_String으로_변환한다() {
        // given
        Identifier identifier = new Identifier("user123");

        // when
        String result = converter.convertToDatabaseColumn(identifier);

        // then
        assertEquals("user123", result);
    }

    @Test
    void null_Identifier를_null_String으로_변환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertNull(result);
    }

    @Test
    void String을_Identifier_값_객체로_변환한다() {
        // given
        String dbData = "user456";

        // when
        Identifier result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("user456", result.getValue());
    }

    @Test
    void null_String을_null_Identifier로_변환한다() {
        // when
        Identifier result = converter.convertToEntityAttribute(null);

        // then
        assertNull(result);
    }

    @Test
    void 양방향_변환이_정상적으로_동작한다() {
        // given
        Identifier original = new Identifier("testUser");

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        Identifier converted = converter.convertToEntityAttribute(dbData);

        // then
        assertEquals(original, converted);
        assertEquals(original.getValue(), converted.getValue());
    }
}