package net.catsnap.domain.reservation.repository;

import net.catsnap.domain.reservation.entity.DistrictLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictLevelRepository extends JpaRepository<DistrictLevel, Long> {

    DistrictLevel findAllByCityLevelId(Long cityLevelId);
}
