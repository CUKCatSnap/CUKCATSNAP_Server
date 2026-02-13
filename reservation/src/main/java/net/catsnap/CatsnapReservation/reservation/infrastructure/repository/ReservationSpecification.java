package net.catsnap.CatsnapReservation.reservation.infrastructure.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import net.catsnap.CatsnapReservation.reservation.domain.ReservationStatus;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation_;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot_;
import org.springframework.data.jpa.domain.Specification;

/**
 * 예약 조회 Specification
 *
 * <p>도메인 언어로 예약 조회 조건을 표현합니다.
 * JPA Metamodel({@link Reservation_})을 사용하여 타입 안전성을 확보합니다.</p>
 *
 * <h3>구성</h3>
 * <ul>
 *   <li><b>기본 조건</b> - 단일 필터 조건 ({@link #belongsToPhotographer}, {@link #onDate}, {@link #isActive})</li>
 *   <li><b>조합 조건</b> - 기본 조건을 조합하여 도메인 의미를 표현 ({@link #activeReservationsOf})</li>
 * </ul>
 */
public class ReservationSpecification {

    private ReservationSpecification() {
    }

    // ──────────────────────────────────────────────
    //  기본 조건 (Primitive Specifications)
    // ──────────────────────────────────────────────

    /**
     * 특정 작가에게 속한 예약만 필터링합니다.
     *
     * <p>{@code WHERE photographer_id = :photographerId}</p>
     *
     * @param photographerId 조회할 작가 ID
     * @return 해당 작가의 예약만 포함하는 Specification
     */
    public static Specification<Reservation> belongsToPhotographer(Long photographerId) {
        return (root, query, cb) -> cb.equal(root.get(Reservation_.photographerId), photographerId);
    }

    /**
     * 특정 날짜에 시작하는 예약만 필터링합니다.
     *
     * <p>예약의 {@code startDateTime}이 해당 날짜의 00:00:00 ~ 23:59:59 범위에 포함되는지 확인합니다.</p>
     *
     * @param date 조회할 날짜
     * @return 해당 날짜의 예약만 포함하는 Specification
     */
    public static Specification<Reservation> onDate(LocalDate date) {
        return (root, query, cb) -> {
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);
            return cb.and(
                cb.greaterThanOrEqualTo(
                    root.get(Reservation_.timeSlot).get(ReservationTimeSlot_.startDateTime), dayStart),
                cb.lessThanOrEqualTo(
                    root.get(Reservation_.timeSlot).get(ReservationTimeSlot_.startDateTime), dayEnd)
            );
        };
    }

    /**
     * 활성 상태(슬롯을 점유 중인) 예약만 필터링합니다.
     *
     * <p>활성 상태란 {@link ReservationStatus#PENDING} 또는 {@link ReservationStatus#CONFIRMED}을 의미합니다.
     * CANCELED, EXPIRED 예약은 슬롯을 점유하지 않으므로 제외됩니다.</p>
     *
     * @return 활성 예약만 포함하는 Specification
     */
    public static Specification<Reservation> isActive() {
        return (root, query, cb) -> root.get(Reservation_.status)
            .in(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);
    }

    // ──────────────────────────────────────────────
    //  조합 조건 (Composite Specifications)
    // ──────────────────────────────────────────────

    /**
     * 특정 작가의 특정 날짜에 슬롯을 점유 중인 예약을 조회합니다.
     *
     * <p>예약 생성 시 시간대 겹침 검증을 위해 사용됩니다.
     * {@link #belongsToPhotographer}, {@link #onDate}, {@link #isActive}를 조합합니다.</p>
     *
     * @param photographerId 작가 ID
     * @param date           조회할 날짜
     * @return 해당 작가의 해당 날짜 활성 예약을 포함하는 Specification
     */
    public static Specification<Reservation> activeReservationsOf(Long photographerId, LocalDate date) {
        return belongsToPhotographer(photographerId)
            .and(onDate(date))
            .and(isActive());
    }
}