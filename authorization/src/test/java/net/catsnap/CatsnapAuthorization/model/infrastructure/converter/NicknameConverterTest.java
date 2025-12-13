package net.catsnap.CatsnapAuthorization.model.infrastructure.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import net.catsnap.CatsnapAuthorization.model.domain.vo.Nickname;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("NicknameConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class NicknameConverterTest {

    private NicknameConverter converter;

    @BeforeEach
    void setUp() {
        converter = new NicknameConverter();
    }

    @Test
    void Nickname_값_객체를_String으로_변환한다() {
        // given
        Nickname nickname = new Nickname("홍길동");

        // when
        String result = converter.convertToDatabaseColumn(nickname);

        // then
        assertEquals("홍길동", result);
    }

    @Test
    void String을_Nickname_값_객체로_변환한다() {
        // given
        String dbData = "김철수";

        // when
        Nickname result = converter.convertToEntityAttribute(dbData);

        // then
        assertNotNull(result);
        assertEquals("김철수", result.getValue());
    }
}
