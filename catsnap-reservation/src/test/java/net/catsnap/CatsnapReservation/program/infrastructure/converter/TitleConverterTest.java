package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.program.domain.vo.Title;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("TitleConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TitleConverterTest {

    private TitleConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TitleConverter();
    }

    @Test
    void Title을_문자열로_변환한다() {
        // given
        Title title = new Title("웨딩 스냅 촬영");

        // when
        String result = converter.convertToDatabaseColumn(title);

        // then
        assertThat(result).isEqualTo("웨딩 스냅 촬영");
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 문자열을_Title로_변환한다() {
        // given
        String dbData = "웨딩 스냅 촬영";

        // when
        Title result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo("웨딩 스냅 촬영");
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        Title result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Title original = new Title("프로필 촬영");

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        Title restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }
}
