package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdayReservationTimeMappingRepository extends
    JpaRepository<WeekdayReservationTimeMapping, Long> {

    List<WeekdayReservationTimeMapping> findByPhotographerAndReservationTimeFormatId(
        Photographer photographer,
        String reservationTimeFormatId);

    Optional<WeekdayReservationTimeMapping> findByPhotographerIdAndWeekday(Long photographerId,
        Weekday weekday);
}
