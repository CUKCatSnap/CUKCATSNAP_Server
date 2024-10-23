package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllReservationByPhotographerIdAndStartTimeBetween(Long photographerId, LocalDateTime startTime, LocalDateTime endTime);
}
