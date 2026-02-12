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
 * <p>
 * 도메인 언어로 조회 조건을 표현합니다.
 * JPA Metamodel(Reservation_)을 사용하여 타입 안전성을 확보합니다.
 */
public class ReservationSpecification {

    private ReservationSpecification() {
    }

    /**
     * 특정 작가의 예약
     *
     * @param photographerId 작가 ID
     */
    public static Specification<Reservation> belongsToPhotographer(Long photographerId) {
        return (root, query, cb) -> cb.equal(root.get(Reservation_.photographerId), photographerId);
    }

    /**
     * 특정 날짜의 예약 (startDateTime이 해당 날짜 범위에 포함)
     *
     * @param date 조회할 날짜
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
     * 활성 상태 예약 (PENDING 또는 CONFIRMED)
     */
    public static Specification<Reservation> isActive() {
        return (root, query, cb) -> root.get(Reservation_.status)
            .in(ReservationStatus.PENDING, ReservationStatus.CONFIRMED);
    }
}