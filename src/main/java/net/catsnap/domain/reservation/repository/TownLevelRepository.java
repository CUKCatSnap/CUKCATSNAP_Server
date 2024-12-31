package net.catsnap.domain.reservation.repository;

import java.util.Optional;
import net.catsnap.domain.reservation.entity.TownLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownLevelRepository extends JpaRepository<TownLevel, Long> {

    Optional<TownLevel> findTownLevelByTownName(String townName);

    Optional<TownLevel> findTownLevelsByCode(String code);
}
