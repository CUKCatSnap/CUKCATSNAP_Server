package net.catsnap.CatsnapReservation.reservation.domain;

import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_AMOUNT;
import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_HOLD_EXPIRES_AT;
import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_MODEL_ID;
import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_PHOTOGRAPHER_ID;
import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_PROGRAM_ID;
import static net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture.DEFAULT_TIME_SLOT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancellationInfo;
import net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Reservation 엔티티 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationTest {

    @Nested
    @DisplayName("hold() - 임시 예약 생성")
    class Hold {

        @Test
        void 임시_예약을_생성한다() {
            // when
            Reservation reservation = ReservationFixture.createDefault();

            // then
            assertThat(reservation.getReservationNumber()).isNotNull();
            assertThat(reservation.getModelId()).isEqualTo(DEFAULT_MODEL_ID);
            assertThat(reservation.getPhotographerId()).isEqualTo(DEFAULT_PHOTOGRAPHER_ID);
            assertThat(reservation.getProgramId()).isEqualTo(DEFAULT_PROGRAM_ID);
            assertThat(reservation.getTimeSlot()).isEqualTo(DEFAULT_TIME_SLOT);
            assertThat(reservation.getAmount()).isEqualTo(DEFAULT_AMOUNT);
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
            assertThat(reservation.getHoldExpiresAt()).isEqualTo(DEFAULT_HOLD_EXPIRES_AT);
        }

        @Test
        void null_모델ID로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithModelId(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("모델 ID는 필수입니다");
        }

        @Test
        void null_작가ID로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithPhotographerId(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("작가 ID는 필수입니다");
        }

        @Test
        void null_프로그램ID로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithProgramId(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("프로그램 ID는 필수입니다");
        }

        @Test
        void null_시간대로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithTimeSlot(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("예약 시간대는 필수입니다");
        }

        @Test
        void null_금액으로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithAmount(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("예약 금액은 필수입니다");
        }

        @Test
        void null_홀드만료시각으로_생성_시_예외가_발생한다() {
            // when & then
            assertThatThrownBy(() -> ReservationFixture.createWithHoldExpiresAt(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("홀드 만료 시각은 필수입니다");
        }
    }

    @Nested
    @DisplayName("confirm() - 예약 확정")
    class Confirm {

        @Test
        void PENDING_상태에서_확정한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime confirmedAt = LocalDateTime.of(2025, 6, 15, 9, 0);

            // when
            reservation.confirm(confirmedAt);

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CONFIRMED);
            assertThat(reservation.getConfirmedAt()).isEqualTo(confirmedAt);
            assertThat(reservation.getHoldExpiresAt()).isNull();
        }

        @Test
        void 만료된_예약은_확정할_수_없다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime afterExpiry = DEFAULT_HOLD_EXPIRES_AT.plusMinutes(1);

            // when & then
            assertThatThrownBy(() -> reservation.confirm(afterExpiry))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("이미 만료된 예약은 확정할 수 없습니다");
        }

        @Test
        void null_확정시각으로_확정_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThatThrownBy(() -> reservation.confirm(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("확정 시각은 필수입니다");
        }

        @Test
        void CONFIRMED_상태에서_확정_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createConfirmed();

            // when & then
            assertThatThrownBy(() -> reservation.confirm(LocalDateTime.of(2025, 6, 15, 9, 0)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("임시예약 상태에서만 확정할 수 있습니다");
        }
    }

    @Nested
    @DisplayName("cancel() - 예약 취소")
    class Cancel {

        @Test
        void 모델이_PENDING_상태에서_취소한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            CancelReason reason = new CancelReason("개인 사정");
            LocalDateTime canceledAt = LocalDateTime.of(2025, 6, 15, 9, 0);

            // when
            reservation.cancel(CanceledBy.MODEL, reason, canceledAt);

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
            CancellationInfo info = reservation.getCancellationInfo();
            assertThat(info.getCanceledBy()).isEqualTo(CanceledBy.MODEL);
            assertThat(info.getCanceledAt()).isEqualTo(canceledAt);
            assertThat(info.getCancelReason()).isEqualTo(reason);
            assertThat(reservation.getHoldExpiresAt()).isNull();
        }

        @Test
        void 작가가_CONFIRMED_상태에서_취소한다() {
            // given
            Reservation reservation = ReservationFixture.createConfirmed();
            CancelReason reason = new CancelReason("일정 변경");
            LocalDateTime canceledAt = LocalDateTime.of(2025, 6, 15, 9, 10);

            // when
            reservation.cancel(CanceledBy.PHOTOGRAPHER, reason, canceledAt);

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
            assertThat(reservation.getCancellationInfo().getCanceledBy()).isEqualTo(CanceledBy.PHOTOGRAPHER);
        }

        @Test
        void 시스템이_취소한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime canceledAt = LocalDateTime.of(2025, 6, 15, 9, 0);

            // when
            reservation.cancel(CanceledBy.SYSTEM, new CancelReason("시스템 자동 취소"), canceledAt);

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
            assertThat(reservation.getCancellationInfo().getCanceledBy()).isEqualTo(CanceledBy.SYSTEM);
        }

        @Test
        void 이미_취소된_예약은_멱등하게_무시한다() {
            // given
            Reservation reservation = ReservationFixture.createCanceled();

            // when & then
            assertThatCode(() -> reservation.cancel(
                CanceledBy.MODEL, new CancelReason("재취소"), LocalDateTime.of(2025, 6, 15, 9, 5)))
                .doesNotThrowAnyException();
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
        }

        @Test
        void EXPIRED_상태에서_취소_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createExpired();

            // when & then
            assertThatThrownBy(() -> reservation.cancel(
                CanceledBy.MODEL, new CancelReason("사유"), LocalDateTime.of(2025, 6, 15, 10, 1)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("만료된 예약은 취소할 수 없습니다");
        }

        @Test
        void null_취소주체로_취소_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThatThrownBy(() -> reservation.cancel(
                null, new CancelReason("사유"), LocalDateTime.of(2025, 6, 15, 9, 0)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("취소 주체는 필수입니다");
        }

        @Test
        void null_취소시각으로_취소_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThatThrownBy(() -> reservation.cancel(
                CanceledBy.MODEL, new CancelReason("사유"), null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("취소 시각은 필수입니다");
        }
    }

    @Nested
    @DisplayName("expire() - 예약 만료")
    class Expire {

        @Test
        void PENDING_상태에서_만료한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime expiredAt = LocalDateTime.of(2025, 6, 15, 10, 0);

            // when
            reservation.expire(expiredAt);

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.EXPIRED);
            assertThat(reservation.getHoldExpiresAt()).isNull();
        }

        @Test
        void 이미_만료된_예약은_멱등하게_무시한다() {
            // given
            Reservation reservation = ReservationFixture.createExpired();

            // when & then
            assertThatCode(() -> reservation.expire(LocalDateTime.of(2025, 6, 15, 10, 0)))
                .doesNotThrowAnyException();
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.EXPIRED);
        }

        @Test
        void CONFIRMED_상태에서_만료_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createConfirmed();

            // when & then
            assertThatThrownBy(() -> reservation.expire(LocalDateTime.of(2025, 6, 15, 10, 0)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("임시예약 상태에서만 만료 처리할 수 있습니다");
        }

        @Test
        void null_만료시각으로_만료_시_예외가_발생한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThatThrownBy(() -> reservation.expire(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("만료 시각은 필수입니다");
        }

        @Test
        void 홀드_만료_이전에는_만료_처리를_무시한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime beforeExpiry = DEFAULT_HOLD_EXPIRES_AT.minusMinutes(1);

            // when & then
            assertThatCode(() -> reservation.expire(beforeExpiry))
                .doesNotThrowAnyException();
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
            assertThat(reservation.getHoldExpiresAt()).isEqualTo(DEFAULT_HOLD_EXPIRES_AT);
        }
    }

    @Nested
    @DisplayName("isPayableAt() - 결제 가능 여부")
    class IsPayableAt {

        @Test
        void PENDING_상태이고_만료_전이면_결제_가능하다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime beforeExpiry = DEFAULT_HOLD_EXPIRES_AT.minusMinutes(1);

            // when & then
            assertThat(reservation.isPayableAt(beforeExpiry)).isTrue();
        }

        @Test
        void 만료_시각_이후면_결제_불가능하다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();
            LocalDateTime afterExpiry = DEFAULT_HOLD_EXPIRES_AT.plusMinutes(1);

            // when & then
            assertThat(reservation.isPayableAt(afterExpiry)).isFalse();
        }

        @Test
        void CONFIRMED_상태면_결제_불가능하다() {
            // given
            Reservation reservation = ReservationFixture.createConfirmed();

            // when & then
            assertThat(reservation.isPayableAt(LocalDateTime.of(2025, 6, 15, 9, 5))).isFalse();
        }
    }

    @Nested
    @DisplayName("isHoldExpiredAt() - 홀드 만료 여부")
    class IsHoldExpiredAt {

        @Test
        void 만료_시각_전이면_false를_반환한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThat(reservation.isHoldExpiredAt(DEFAULT_HOLD_EXPIRES_AT.minusMinutes(1))).isFalse();
        }

        @Test
        void 만료_시각과_같으면_true를_반환한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThat(reservation.isHoldExpiredAt(DEFAULT_HOLD_EXPIRES_AT)).isTrue();
        }

        @Test
        void 만료_시각_이후면_true를_반환한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThat(reservation.isHoldExpiredAt(DEFAULT_HOLD_EXPIRES_AT.plusMinutes(1))).isTrue();
        }
    }

    @Nested
    @DisplayName("소유권 확인")
    class Ownership {

        @Test
        void 모델_소유자를_확인한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThat(reservation.isOwnedByModel(DEFAULT_MODEL_ID)).isTrue();
            assertThat(reservation.isOwnedByModel(999L)).isFalse();
        }

        @Test
        void 작가_소유자를_확인한다() {
            // given
            Reservation reservation = ReservationFixture.createDefault();

            // when & then
            assertThat(reservation.isOwnedByPhotographer(DEFAULT_PHOTOGRAPHER_ID)).isTrue();
            assertThat(reservation.isOwnedByPhotographer(999L)).isFalse();
        }
    }
}
