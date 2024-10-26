package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeekdayReservationTimeMappingRepository extends JpaRepository<WeekdayReservationTimeMapping, Long> {
    @Modifying
    @Query("UPDATE WeekdayReservationTimeMapping m  SET m.reservationTimeFormatId = null WHERE m.photographer.id = :photographerId AND m.reservationTimeFormatId = :reservationTimeFormatId")
    void updateReservationTimeFormatIdToNull(@Param("photographerId") Long photographerId, @Param("reservationTimeFormatId") String reservationTimeFormatId);

    Optional<WeekdayReservationTimeMapping> findByPhotographerIdAndWeekday(Long photographerId, Weekday weekday);
}
