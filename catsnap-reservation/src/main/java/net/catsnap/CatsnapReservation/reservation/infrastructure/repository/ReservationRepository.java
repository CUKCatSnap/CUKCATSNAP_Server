package net.catsnap.CatsnapReservation.reservation.infrastructure.repository;

import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 예약 Repository
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long>,
    JpaSpecificationExecutor<Reservation> {
}