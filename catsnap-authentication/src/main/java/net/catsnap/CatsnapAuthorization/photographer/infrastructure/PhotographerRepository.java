package net.catsnap.CatsnapAuthorization.photographer.infrastructure;

import java.util.Optional;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Long> {

    boolean existsByIdentifier(Identifier identifier);

    Optional<Photographer> findByIdentifier(Identifier identifier);
}