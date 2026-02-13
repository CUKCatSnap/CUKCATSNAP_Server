package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.program.domain.vo.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("DescriptionConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DescriptionConverterTest {

    private DescriptionConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DescriptionConverter();
    }

    @Test
    void Description을_문자열로_변환한다() {
        // given
        Description description = new Description("아름다운 웨딩 스냅입니다.");

        // when
        String result = converter.convertToDatabaseColumn(description);

        // then
        assertThat(result).isEqualTo("아름다운 웨딩 스냅입니다.");
    }

    @Test
    void null_Description을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        String result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void null_값을_가진_Description을_DB_컬럼으로_변환하면_null을_반환한다() {
        // given
        Description description = new Description(null);

        // when
        String result = converter.convertToDatabaseColumn(description);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 문자열을_Description으로_변환한다() {
        // given
        String dbData = "아름다운 웨딩 스냅입니다.";

        // when
        Description result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo("아름다운 웨딩 스냅입니다.");
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        Description result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Description original = new Description("프로필 촬영 설명입니다.");

        // when
        String dbData = converter.convertToDatabaseColumn(original);
        Description restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }
}
