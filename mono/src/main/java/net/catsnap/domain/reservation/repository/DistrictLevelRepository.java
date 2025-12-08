package net.catsnap.domain.reservation.repository;

import net.catsnap.domain.reservation.entity.DistrictLevel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictLevelRepository extends JpaRepository<DistrictLevel, Long> {

    Slice<DistrictLevel> findAllByCityLevelId(Long cityLevelId, Pageable pageable);
}
