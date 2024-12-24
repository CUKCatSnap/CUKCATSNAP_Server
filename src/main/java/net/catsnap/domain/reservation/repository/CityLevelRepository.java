package net.catsnap.domain.reservation.repository;

import java.util.Optional;
import net.catsnap.domain.reservation.entity.CityLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityLevelRepository extends JpaRepository<CityLevel, Long> {

    Optional<CityLevel> findCityLevelByCityName(String cityName);
}
