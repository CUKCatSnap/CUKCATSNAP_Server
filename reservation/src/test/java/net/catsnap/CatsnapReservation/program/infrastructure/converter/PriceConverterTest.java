package net.catsnap.CatsnapReservation.program.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.program.domain.vo.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("PriceConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceConverterTest {

    private PriceConverter converter;

    @BeforeEach
    void setUp() {
        converter = new PriceConverter();
    }

    @Test
    void Price를_Long으로_변환한다() {
        // given
        Price price = new Price(150000L);

        // when
        Long result = converter.convertToDatabaseColumn(price);

        // then
        assertThat(result).isEqualTo(150000L);
    }

    @Test
    void null을_DB_컬럼으로_변환하면_null을_반환한다() {
        // when
        Long result = converter.convertToDatabaseColumn(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void Long을_Price로_변환한다() {
        // given
        Long dbData = 150000L;

        // when
        Price result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(150000L);
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        Price result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Price original = new Price(200000L);

        // when
        Long dbData = converter.convertToDatabaseColumn(original);
        Price restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }

    @Test
    void 영원_가격도_양방향_변환이_동작한다() {
        // given
        Price original = new Price(0L);

        // when
        Long dbData = converter.convertToDatabaseColumn(original);
        Price restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
        assertThat(restored.isFree()).isTrue();
    }
}
