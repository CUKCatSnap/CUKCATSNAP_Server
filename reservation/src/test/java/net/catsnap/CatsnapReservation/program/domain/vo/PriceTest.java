package net.catsnap.CatsnapReservation.program.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Price VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 유효한_가격으로_생성에_성공한다() {
        // given
        Long value = 150000L;

        // when
        Price price = new Price(value);

        // then
        assertThat(price.getValue()).isEqualTo(value);
    }

    @Test
    void 영원으로_생성에_성공한다() {
        // given
        Long value = 0L;

        // when
        Price price = new Price(value);

        // then
        assertThat(price.getValue()).isEqualTo(0L);
        assertThat(price.isFree()).isTrue();
    }

    @Test
    void 유료_가격은_isFree가_false를_반환한다() {
        // given
        Price price = new Price(10000L);

        // when & then
        assertThat(price.isFree()).isFalse();
    }

    @Test
    void null_가격으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Price(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("가격은 필수입니다");
    }

    @Test
    void 음수_가격으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Price(-1L))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("0원 이상");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        Price price1 = new Price(150000L);
        Price price2 = new Price(150000L);

        // when & then
        assertThat(price1).isEqualTo(price2);
        assertThat(price1.hashCode()).isEqualTo(price2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        Price price1 = new Price(150000L);
        Price price2 = new Price(200000L);

        // when & then
        assertThat(price1).isNotEqualTo(price2);
    }

    @Test
    void toString이_원_단위로_값을_반환한다() {
        // given
        Price price = new Price(150000L);

        // when
        String result = price.toString();

        // then
        assertThat(result).isEqualTo("150000원");
    }
}
