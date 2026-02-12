package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("Money VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MoneyTest {

    @Test
    void 유효한_금액으로_생성에_성공한다() {
        // given
        Long value = 150000L;

        // when
        Money money = new Money(value);

        // then
        assertThat(money.getValue()).isEqualTo(value);
    }

    @Test
    void 영원으로_생성에_성공한다() {
        // given & when
        Money money = new Money(0L);

        // then
        assertThat(money.getValue()).isEqualTo(0L);
        assertThat(money.isFree()).isTrue();
    }

    @Test
    void 유료_금액은_isFree가_false를_반환한다() {
        // given
        Money money = new Money(10000L);

        // when & then
        assertThat(money.isFree()).isFalse();
    }

    @Test
    void null_금액으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Money(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("금액은 필수입니다");
    }

    @Test
    void 음수_금액으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new Money(-1L))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("0원 이상");
    }

    @Test
    void add로_금액을_더한다() {
        // given
        Money money1 = new Money(10000L);
        Money money2 = new Money(5000L);

        // when
        Money result = money1.add(money2);

        // then
        assertThat(result.getValue()).isEqualTo(15000L);
    }

    @Test
    void add에_null을_전달하면_예외가_발생한다() {
        // given
        Money money = new Money(10000L);

        // when & then
        assertThatThrownBy(() -> money.add(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("더할 금액은 필수입니다");
    }

    @Test
    void subtract로_금액을_뺀다() {
        // given
        Money money1 = new Money(10000L);
        Money money2 = new Money(3000L);

        // when
        Money result = money1.subtract(money2);

        // then
        assertThat(result.getValue()).isEqualTo(7000L);
    }

    @Test
    void subtract_결과가_음수면_예외가_발생한다() {
        // given
        Money money1 = new Money(3000L);
        Money money2 = new Money(10000L);

        // when & then
        assertThatThrownBy(() -> money1.subtract(money2))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("0원 이상");
    }

    @Test
    void subtract에_null을_전달하면_예외가_발생한다() {
        // given
        Money money = new Money(10000L);

        // when & then
        assertThatThrownBy(() -> money.subtract(null))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("뺄 금액은 필수입니다");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        Money money1 = new Money(150000L);
        Money money2 = new Money(150000L);

        // when & then
        assertThat(money1).isEqualTo(money2);
        assertThat(money1.hashCode()).isEqualTo(money2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        Money money1 = new Money(150000L);
        Money money2 = new Money(200000L);

        // when & then
        assertThat(money1).isNotEqualTo(money2);
    }

    @Test
    void toString이_원_단위로_값을_반환한다() {
        // given
        Money money = new Money(150000L);

        // when & then
        assertThat(money.toString()).isEqualTo("150000원");
    }
}
