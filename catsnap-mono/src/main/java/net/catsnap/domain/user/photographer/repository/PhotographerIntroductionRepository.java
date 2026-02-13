package net.catsnap.domain.user.photographer.repository;

import java.util.Optional;
import net.catsnap.domain.user.photographer.entity.PhotographerIntroduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerIntroductionRepository extends
    JpaRepository<PhotographerIntroduction, Long> {

    Optional<PhotographerIntroduction> findByPhotographerId(Long photographerId);
}
