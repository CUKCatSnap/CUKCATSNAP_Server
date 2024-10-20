package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekdayReservationTimeMappingRepository extends JpaRepository<WeekdayReservationTimeMapping, Long> {
}
