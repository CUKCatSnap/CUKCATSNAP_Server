package net.catsnap.CatsnapReservation.reservation.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import net.catsnap.CatsnapReservation.reservation.domain.vo.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("MoneyConverter 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyConverterTest {

    private MoneyConverter converter;

    @BeforeEach
    void setUp() {
        converter = new MoneyConverter();
    }

    @Test
    void Money를_Long으로_변환한다() {
        // given
        Money money = new Money(150000L);

        // when
        Long result = converter.convertToDatabaseColumn(money);

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
    void Long을_Money로_변환한다() {
        // given
        Long dbData = 150000L;

        // when
        Money result = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getValue()).isEqualTo(150000L);
    }

    @Test
    void null을_엔티티_속성으로_변환하면_null을_반환한다() {
        // when
        Money result = converter.convertToEntityAttribute(null);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 양방향_변환이_올바르게_동작한다() {
        // given
        Money original = new Money(200000L);

        // when
        Long dbData = converter.convertToDatabaseColumn(original);
        Money restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
    }

    @Test
    void 영원_금액도_양방향_변환이_동작한다() {
        // given
        Money original = new Money(0L);

        // when
        Long dbData = converter.convertToDatabaseColumn(original);
        Money restored = converter.convertToEntityAttribute(dbData);

        // then
        assertThat(restored).isEqualTo(original);
        assertThat(restored.isFree()).isTrue();
    }
}
