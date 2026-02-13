package net.catsnap.CatsnapReservation.reservation.fixture;

import java.time.LocalDateTime;
import net.catsnap.CatsnapReservation.reservation.domain.CanceledBy;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;
import net.catsnap.CatsnapReservation.reservation.domain.vo.Money;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot;

/**
 * Reservation 테스트용 Fixture
 */
public class ReservationFixture {

    public static final Long DEFAULT_MODEL_ID = 1L;
    public static final Long DEFAULT_PHOTOGRAPHER_ID = 2L;
    public static final Long DEFAULT_PROGRAM_ID = 3L;
    public static final ReservationTimeSlot DEFAULT_TIME_SLOT = new ReservationTimeSlot(
        LocalDateTime.of(2025, 6, 15, 10, 0),
        LocalDateTime.of(2025, 6, 15, 12, 0));
    public static final Money DEFAULT_AMOUNT = new Money(150000L);
    public static final LocalDateTime DEFAULT_HOLD_EXPIRES_AT = LocalDateTime.of(2025, 6, 15, 9, 30);

    /**
     * 기본 PENDING 상태 예약 생성
     */
    public static Reservation createDefault() {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_PROGRAM_ID,
            DEFAULT_TIME_SLOT,
            DEFAULT_AMOUNT,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 modelId로 PENDING 예약 생성
     */
    public static Reservation createWithModelId(Long modelId) {
        return Reservation.hold(
            modelId,
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_PROGRAM_ID,
            DEFAULT_TIME_SLOT,
            DEFAULT_AMOUNT,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 photographerId로 PENDING 예약 생성
     */
    public static Reservation createWithPhotographerId(Long photographerId) {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            photographerId,
            DEFAULT_PROGRAM_ID,
            DEFAULT_TIME_SLOT,
            DEFAULT_AMOUNT,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 programId로 PENDING 예약 생성
     */
    public static Reservation createWithProgramId(Long programId) {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            DEFAULT_PHOTOGRAPHER_ID,
            programId,
            DEFAULT_TIME_SLOT,
            DEFAULT_AMOUNT,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 timeSlot으로 PENDING 예약 생성
     */
    public static Reservation createWithTimeSlot(ReservationTimeSlot timeSlot) {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_PROGRAM_ID,
            timeSlot,
            DEFAULT_AMOUNT,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 금액으로 PENDING 예약 생성
     */
    public static Reservation createWithAmount(Money amount) {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_PROGRAM_ID,
            DEFAULT_TIME_SLOT,
            amount,
            DEFAULT_HOLD_EXPIRES_AT
        );
    }

    /**
     * 지정된 holdExpiresAt으로 PENDING 예약 생성
     */
    public static Reservation createWithHoldExpiresAt(LocalDateTime holdExpiresAt) {
        return Reservation.hold(
            DEFAULT_MODEL_ID,
            DEFAULT_PHOTOGRAPHER_ID,
            DEFAULT_PROGRAM_ID,
            DEFAULT_TIME_SLOT,
            DEFAULT_AMOUNT,
            holdExpiresAt
        );
    }

    /**
     * CONFIRMED 상태 예약 생성
     */
    public static Reservation createConfirmed() {
        Reservation reservation = createDefault();
        reservation.confirm(LocalDateTime.of(2025, 6, 15, 9, 0));
        return reservation;
    }

    /**
     * 모델이 취소한 CANCELED 상태 예약 생성
     */
    public static Reservation createCanceled() {
        return createCanceledBy(CanceledBy.MODEL);
    }

    /**
     * 지정된 주체가 취소한 CANCELED 상태 예약 생성
     */
    public static Reservation createCanceledBy(CanceledBy canceledBy) {
        Reservation reservation = createDefault();
        reservation.cancel(canceledBy, new CancelReason("테스트 취소"), LocalDateTime.of(2025, 6, 15, 9, 0));
        return reservation;
    }

    /**
     * EXPIRED 상태 예약 생성
     */
    public static Reservation createExpired() {
        Reservation reservation = createDefault();
        reservation.expire(LocalDateTime.of(2025, 6, 15, 10, 0));
        return reservation;
    }
}