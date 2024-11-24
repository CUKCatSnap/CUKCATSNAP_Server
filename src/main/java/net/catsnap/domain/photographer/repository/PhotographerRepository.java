package net.catsnap.domain.photographer.repository;

import net.catsnap.domain.photographer.entity.Photographer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    Optional<Photographer> findByIdentifier(String identifier);
}
