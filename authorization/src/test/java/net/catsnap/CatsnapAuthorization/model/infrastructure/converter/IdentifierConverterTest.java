package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void String을_Identifier_값_객체로_변환한다() {
        // given
        String dbData = "user456";

        // when
        Identifier result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("user456", result.getValue());
    }
}