package net.catsnap.CatsnapReservation.reservation.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.reservation.domain.CanceledBy;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayName("CancellationInfo VO 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CancellationInfoTest {

    private static final LocalDateTime CANCELED_AT = LocalDateTime.of(2025, 6, 15, 9, 0);

    @Test
    void 유효한_값으로_생성에_성공한다() {
        // given
        CancelReason reason = new CancelReason("개인 사정");

        // when
        CancellationInfo info = new CancellationInfo(CanceledBy.MODEL, CANCELED_AT, reason);

        // then
        assertThat(info.getCanceledBy()).isEqualTo(CanceledBy.MODEL);
        assertThat(info.getCanceledAt()).isEqualTo(CANCELED_AT);
        assertThat(info.getCancelReason()).isEqualTo(reason);
    }

    @Test
    void 취소사유_없이_생성에_성공한다() {
        // when
        CancellationInfo info = new CancellationInfo(CanceledBy.SYSTEM, CANCELED_AT, null);

        // then
        assertThat(info.getCanceledBy()).isEqualTo(CanceledBy.SYSTEM);
        assertThat(info.getCancelReason()).isNull();
    }

    @Test
    void null_취소주체로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new CancellationInfo(null, CANCELED_AT, new CancelReason("사유")))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("취소 주체는 필수입니다");
    }

    @Test
    void null_취소시각으로_생성_시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new CancellationInfo(CanceledBy.MODEL, null, new CancelReason("사유")))
            .isInstanceOf(DomainException.class)
            .hasMessageContaining("취소 시각은 필수입니다");
    }

    @Test
    void 동일한_값을_가진_객체는_같다() {
        // given
        CancelReason reason = new CancelReason("사유");
        CancellationInfo info1 = new CancellationInfo(CanceledBy.MODEL, CANCELED_AT, reason);
        CancellationInfo info2 = new CancellationInfo(CanceledBy.MODEL, CANCELED_AT, reason);

        // when & then
        assertThat(info1).isEqualTo(info2);
        assertThat(info1.hashCode()).isEqualTo(info2.hashCode());
    }

    @Test
    void 취소주체가_다르면_다른_객체이다() {
        // given
        CancelReason reason = new CancelReason("사유");
        CancellationInfo info1 = new CancellationInfo(CanceledBy.MODEL, CANCELED_AT, reason);
        CancellationInfo info2 = new CancellationInfo(CanceledBy.PHOTOGRAPHER, CANCELED_AT, reason);

        // when & then
        assertThat(info1).isNotEqualTo(info2);
    }
}
