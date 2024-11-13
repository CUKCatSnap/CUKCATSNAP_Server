package com.cuk.catsnap.domain.reservation.repository;


import com.cuk.catsnap.domain.reservation.entity.Program;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findByPhotographerIdAndDeletedFalse(Long photographerId);

    Optional<Program> findByIdAndPhotographerId(Long id, Long photographerId);
}
