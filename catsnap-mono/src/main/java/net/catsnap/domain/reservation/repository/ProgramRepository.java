package net.catsnap.domain.reservation.repository;


import net.catsnap.domain.reservation.entity.Program;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByPhotographerIdAndDeletedFalse(Long photographerId);
}
