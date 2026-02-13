package net.catsnap.CatsnapReservation.reservation.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapReservation.reservation.domain.CanceledBy;
import net.catsnap.CatsnapReservation.reservation.infrastructure.converter.CancelReasonConverter;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 예약 취소 정보 값 객체 (Value Object)
 *
 * <p>취소 주체, 취소 시각, 취소 사유를 하나의 응집된 개념으로 관리합니다.
 * 예약이 취소되지 않은 경우 Reservation에서 null로 유지됩니다.</p>
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CancellationInfo {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CanceledBy canceledBy;

    private LocalDateTime canceledAt;

    @Convert(converter = CancelReasonConverter.class)
    @Column(length = 300)
    private CancelReason cancelReason;

    public CancellationInfo(CanceledBy canceledBy, LocalDateTime canceledAt, CancelReason cancelReason) {
        validate(canceledBy, canceledAt);
        this.canceledBy = canceledBy;
        this.canceledAt = canceledAt;
        this.cancelReason = cancelReason;
    }

    private void validate(CanceledBy canceledBy, LocalDateTime canceledAt) {
        if (canceledBy == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "취소 주체는 필수입니다.");
        }
        if (canceledAt == null) {
            throw new DomainException(DomainErrorCode.DOMAIN_CONSTRAINT_VIOLATION, "취소 시각은 필수입니다.");
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
        CancellationInfo that = (CancellationInfo) o;
        return canceledBy == that.canceledBy
            && Objects.equals(canceledAt, that.canceledAt)
            && Objects.equals(cancelReason, that.cancelReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(canceledBy, canceledAt, cancelReason);
    }

    @Override
    public String toString() {
        return String.format("CancellationInfo{canceledBy=%s, canceledAt=%s, cancelReason=%s}",
            canceledBy, canceledAt, cancelReason);
    }
}
