package net.catsnap.CatsnapAuthorization.photographer.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.catsnap.CatsnapAuthorization.photographer.domain.vo.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("NameConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NameConverterTest {

    private NameConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NameConverter();
    }

    @Test
    void Name_값_객체를_String으로_변환한다() {
        // given
        Name name = new Name("홍길동");

        // when
        String result = converter.convertToDatabaseColumn(name);

        // then
        assertEquals("홍길동", result);
    }

    @Test
    void String을_Name_값_객체로_변환한다() {
        // given
        String dbData = "김철수";

        // when
        Name result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("김철수", result.getValue());
    }
}