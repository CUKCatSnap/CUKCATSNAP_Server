package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.program.domain.vo.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("DurationConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DurationConverterTest {

    private DurationConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DurationConverter();
    }

    @Test
    void Duration을_Integer로_변환한다() {
        // given
        Duration duration = new Duration(90);

        // when
        Integer result = converter.convertToDatabaseColumn(duration);

        // then
        assertThat(result).isEqualTo(90);
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        Integer result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void Integer를_Duration으로_변환한다() {
        // given
        Integer dbData = 90;

        // when
        Duration result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(90);
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        Duration result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Duration original = new Duration(120);

        // when
        Integer dbData = converter.convertToDatabaseColumn(original);
        Duration restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }
}
