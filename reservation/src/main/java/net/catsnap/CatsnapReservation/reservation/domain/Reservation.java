package net.catsnap.CatsnapReservation.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancellationInfo;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;
import net.catsnap.CatsnapReservation.reservation.domain.vo.Money;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationNumber;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot;
import net.catsnap.CatsnapReservation.reservation.infrastructure.converter.MoneyConverter;
import net.catsnap.CatsnapReservation.reservation.infrastructure.converter.ReservationNumberConverter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 예약 Aggregate Root
 *
 * <p>모델(고객)이 작가(사진작가)의 프로그램을 예약할 때 생성되는 도메인 엔티티입니다.
 * 결제 전 임시 점유(HOLD) 상태를 가지며, 최종적으로 확정/취소/만료로 전이됩니다.</p>
 *
 * <h3>상태 전이 다이어그램</h3>
 * <pre>
 *   hold()       confirm()
 * ────────► PENDING ────────► CONFIRMED
 *              │                  │
 *              │ expire()         │ cancel()
 *              ▼                  ▼
 *           EXPIRED            CANCELED
 *              ▲                  ▲
 *              │   cancel()       │
 *              └──── PENDING ─────┘
 * </pre>
 *
 * <h3>멱등성 보장</h3>
 * <ul>
 *   <li>{@link #cancel} - 이미 CANCELED 상태이면 무시</li>
 *   <li>{@link #expire} - 이미 EXPIRED 상태이면 무시</li>
 * </ul>
 *
 * <h3>동시성 제어</h3>
 * <p>{@code @Version}을 통한 낙관적 잠금으로 동일 슬롯에 대한 동시 예약을 방지합니다.</p>
 *
 * @see ReservationStatus
 * @see ReservationNumber
 * @see ReservationTimeSlot
 * @see Money
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 낙관적 잠금을 위한 버전 필드.
     * 동일 슬롯에 대한 동시 변경 시 {@link jakarta.persistence.OptimisticLockException}을 발생시킵니다.
     */
    @Version
    private Long version;

    /**
     * 외부 노출용 예약 번호 (UUID).
     * 내부 PK 대신 이 값을 API 응답이나 결제 연동에 사용합니다.
     */
    @Convert(converter = ReservationNumberConverter.class)
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private ReservationNumber reservationNumber;

    /** 예약을 요청한 모델(고객)의 ID */
    @Column(nullable = false)
    private Long modelId;

    /** 촬영을 수행하는 작가의 ID */
    @Column(nullable = false)
    private Long photographerId;

    /** 예약 대상 프로그램의 ID */
    @Column(nullable = false)
    private Long programId;

    /** 예약 날짜 및 시간대 (날짜, 시작 시간, 종료 시간) */
    @Embedded
    private ReservationTimeSlot timeSlot;

    /**
     * 예약 시점의 프로그램 가격 스냅샷.
     * 결제 요청 및 환불 차액 계산의 기준 금액으로 사용됩니다.
     */
    @Convert(converter = MoneyConverter.class)
    @Column(nullable = false)
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    /**
     * 임시 점유(HOLD) 만료 시각.
     * 이 시각까지 결제를 완료하지 않으면 슬롯이 해제됩니다.
     * 상태가 PENDING이 아닌 경우 null입니다.
     */
    private LocalDateTime holdExpiresAt;

    /** 예약 확정 시각. CONFIRMED 전이 시 기록됩니다. */
    private LocalDateTime confirmedAt;

    /**
     * 취소 정보 (취소 주체, 취소 시각, 취소 사유).
     * 취소되지 않은 예약에서는 null입니다.
     */
    @Embedded
    private CancellationInfo cancellationInfo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Reservation(
        ReservationNumber reservationNumber,
        Long modelId,
        Long photographerId,
        Long programId,
        ReservationTimeSlot timeSlot,
        Money amount,
        LocalDateTime holdExpiresAt
    ) {
        validateIds(modelId, photographerId, programId);
        validateHoldExpiresAt(holdExpiresAt);

        this.reservationNumber = reservationNumber;
        this.modelId = modelId;
        this.photographerId = photographerId;
        this.programId = programId;
        this.timeSlot = timeSlot;
        this.amount = amount;
        this.status = ReservationStatus.PENDING;
        this.holdExpiresAt = holdExpiresAt;
    }

    /**
     * 임시 예약(HOLD)을 생성합니다.
     *
     * <p>슬롯을 선점하고 {@code holdExpiresAt}까지 결제를 기다립니다.
     * 생성 시 상태는 {@link ReservationStatus#PENDING}으로 설정됩니다.</p>
     *
     * @param modelId        예약 요청 모델(고객) ID
     * @param photographerId 촬영 작가 ID
     * @param programId      예약 대상 프로그램 ID
     * @param timeSlot       예약 날짜 및 시간대
     * @param amount         예약 시점의 프로그램 가격 스냅샷
     * @param holdExpiresAt  임시 점유 만료 시각
     * @return PENDING 상태의 새 예약
     * @throws DomainException ID가 유효하지 않거나 holdExpiresAt이 null인 경우
     */
    public static Reservation hold(
        Long modelId,
        Long photographerId,
        Long programId,
        ReservationTimeSlot timeSlot,
        Money amount,
        LocalDateTime holdExpiresAt
    ) {
        return new Reservation(
            ReservationNumber.generate(),
            modelId,
            photographerId,
            programId,
            timeSlot,
            amount,
            holdExpiresAt
        );
    }

    /**
     * 예약을 확정 상태로 전이합니다.
     *
     * <p>결제가 성공한 후 호출됩니다. PENDING 상태이면서 홀드가 만료되지 않은 경우에만 전이 가능합니다.</p>
     *
     * @param confirmedAt 확정 시각
     * @throws DomainException PENDING 상태가 아니거나 홀드가 만료된 경우
     */
    public void confirm(LocalDateTime confirmedAt) {
        if (confirmedAt == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "확정 시각은 필수입니다.");
        }
        ensurePending("임시예약 상태에서만 확정할 수 있습니다.");

        if (isHoldExpiredAt(confirmedAt)) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "이미 만료된 예약은 확정할 수 없습니다.");
        }

        this.status = ReservationStatus.CONFIRMED;
        this.confirmedAt = confirmedAt;
        this.holdExpiresAt = null;
    }

    /**
     * 예약을 취소 상태로 전이합니다.
     *
     * <p>PENDING 또는 CONFIRMED 상태에서 전이 가능합니다.
     * 이미 취소된 예약은 멱등하게 무시합니다.</p>
     *
     * @param canceledBy   취소 주체 (MODEL, PHOTOGRAPHER, SYSTEM)
     * @param cancelReason 취소 사유 (nullable 허용)
     * @param canceledAt   취소 시각
     * @throws DomainException EXPIRED 상태이거나 취소 불가능한 상태인 경우
     */
    public void cancel(CanceledBy canceledBy, CancelReason cancelReason, LocalDateTime canceledAt) {
        if (this.status == ReservationStatus.CANCELED) {
            return;
        }
        if (this.status == ReservationStatus.EXPIRED) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "만료된 예약은 취소할 수 없습니다.");
        }
        if (this.status != ReservationStatus.PENDING && this.status != ReservationStatus.CONFIRMED) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "현재 상태에서는 예약을 취소할 수 없습니다.");
        }

        this.status = ReservationStatus.CANCELED;
        this.cancellationInfo = new CancellationInfo(canceledBy, canceledAt, cancelReason);
        this.holdExpiresAt = null;
    }

    /**
     * 예약을 만료 상태로 전이합니다.
     *
     * <p>홀드 만료 시각이 지난 PENDING 예약에 대해 만료 처리를 수행합니다.
     * 홀드 만료 이전 또는 이미 만료된 예약은 멱등하게 무시합니다.</p>
     *
     * @param expiredAt 만료 처리 시각
     * @throws DomainException PENDING 상태가 아닌 경우
     */
    public void expire(LocalDateTime expiredAt) {
        if (expiredAt == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "만료 시각은 필수입니다.");
        }

        if (this.status == ReservationStatus.EXPIRED) {
            return;
        }
        ensurePending("임시예약 상태에서만 만료 처리할 수 있습니다.");
        if (!isHoldExpiredAt(expiredAt)) {
            return;
        }

        this.status = ReservationStatus.EXPIRED;
        this.holdExpiresAt = null;
    }

    /**
     * 현재 시각 기준으로 결제 가능 여부를 확인합니다.
     *
     * <p>PENDING 상태이면서 홀드가 만료되지 않은 경우에만 {@code true}를 반환합니다.</p>
     *
     * @param now 현재 시각
     * @return 결제 가능 여부
     */
    public boolean isPayableAt(LocalDateTime now) {
        if (now == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "현재 시각은 필수입니다.");
        }
        return this.status == ReservationStatus.PENDING && !isHoldExpiredAt(now);
    }

    /**
     * 지정된 시각 기준으로 홀드 만료 여부를 확인합니다.
     *
     * <p>{@code holdExpiresAt}이 null인 경우(이미 상태 전이가 완료된 경우) {@code false}를 반환합니다.</p>
     *
     * @param now 기준 시각
     * @return {@code now >= holdExpiresAt}이면 {@code true}
     */
    public boolean isHoldExpiredAt(LocalDateTime now) {
        if (now == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "현재 시각은 필수입니다.");
        }
        if (this.holdExpiresAt == null) {
            return false;
        }
        return !now.isBefore(this.holdExpiresAt);
    }

    /**
     * 해당 모델(고객)의 예약인지 확인합니다.
     *
     * @param modelId 확인할 모델 ID
     * @return 소유 여부
     */
    public boolean isOwnedByModel(Long modelId) {
        return this.modelId.equals(modelId);
    }

    /**
     * 해당 작가의 예약인지 확인합니다.
     *
     * @param photographerId 확인할 작가 ID
     * @return 소유 여부
     */
    public boolean isOwnedByPhotographer(Long photographerId) {
        return this.photographerId.equals(photographerId);
    }

    private void ensurePending(String message) {
        if (this.status != ReservationStatus.PENDING) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    private void validateIds(Long modelId, Long photographerId, Long programId) {
        validatePositive(modelId, "모델 ID는 필수입니다.");
        validatePositive(photographerId, "작가 ID는 필수입니다.");
        validatePositive(programId, "프로그램 ID는 필수입니다.");
    }

    private void validateHoldExpiresAt(LocalDateTime holdExpiresAt) {
        if (holdExpiresAt == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "홀드 만료 시각은 필수입니다.");
        }
    }

    private void validatePositive(Long value, String message) {
        if (value == null || value <= 0) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Reservation{id=%d, reservationNumber=%s, status=%s, modelId=%d, photographerId=%d}",
            id, reservationNumber, status, modelId, photographerId
        );
    }
}
