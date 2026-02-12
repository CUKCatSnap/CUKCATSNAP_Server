package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CancelReason VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CancelReasonTest {

    @Test
    void 유효한_사유로_생성에_성공한다() {
        // given
        String value = "개인 사정으로 취소합니다.";

        // when
        CancelReason reason = new CancelReason(value);

        // then
        assertThat(reason.getValue()).isEqualTo(value);
    }

    @Test
    void null로_생성에_성공한다() {
        // when
        CancelReason reason = new CancelReason(null);

        // then
        assertThat(reason.getValue()).isNull();
        assertThat(reason.isEmpty()).isTrue();
    }

    @Test
    void 빈_문자열로_생성에_성공한다() {
        // when
        CancelReason reason = new CancelReason("");

        // then
        assertThat(reason.getValue()).isEmpty();
        assertThat(reason.isEmpty()).isTrue();
    }

    @Test
    void 값이_있으면_isEmpty가_false를_반환한다() {
        // given
        CancelReason reason = new CancelReason("취소 사유");

        // when & then
        assertThat(reason.isEmpty()).isFalse();
    }

    @Test
    void 최대_길이를_초과하면_예외가_발생한다() {
        // given
        String longReason = "가".repeat(301);

        // when & then
        assertThatThrownBy(() -> new CancelReason(longReason))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("300자 이하");
    }

    @Test
    void 최대_길이_이하면_생성에_성공한다() {
        // given
        String maxReason = "가".repeat(300);

        // when
        CancelReason reason = new CancelReason(maxReason);

        // then
        assertThat(reason.getValue()).hasSize(300);
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        CancelReason reason1 = new CancelReason("취소 사유");
        CancelReason reason2 = new CancelReason("취소 사유");

        // when & then
        assertThat(reason1).isEqualTo(reason2);
        assertThat(reason1.hashCode()).isEqualTo(reason2.hashCode());
    }

    @Test
    void 다른_값을_가진_객체는_다르다() {
        // given
        CancelReason reason1 = new CancelReason("사유1");
        CancelReason reason2 = new CancelReason("사유2");

        // when & then
        assertThat(reason1).isNotEqualTo(reason2);
    }

    @Test
    void toString이_값을_반환한다() {
        // given
        CancelReason reason = new CancelReason("취소합니다");

        // when & then
        assertThat(reason.toString()).isEqualTo("취소합니다");
    }

    @Test
    void null인_경우_toString이_빈문자열을_반환한다() {
        // given
        CancelReason reason = new CancelReason(null);

        // when & then
        assertThat(reason.toString()).isEmpty();
    }
}
