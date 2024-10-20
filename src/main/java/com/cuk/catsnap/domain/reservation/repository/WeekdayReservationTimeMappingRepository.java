package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdayReservationTimeMappingRepository extends JpaRepository<WeekdayReservationTimeMapping, Long> {
    @Modifying
    @Query("UPDATE WeekdayReservationTimeMapping m  SET m.reservationTimeFormatId = null WHERE m.photographerId = :photographerId AND m.reservationTimeFormatId = :reservationTimeFormatId")
    void updateReservationTimeFormatIdToNull(Long photographerId, String reservationTimeFormatId);
}
