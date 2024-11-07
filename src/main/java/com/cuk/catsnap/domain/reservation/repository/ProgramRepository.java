package com.cuk.catsnap.domain.reservation.repository;


import com.cuk.catsnap.domain.reservation.entity.Program;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    @Modifying
    @Query("UPDATE Program p SET p.deleted = true WHERE p.id = :programId and p.photographer.id = :photographerId")
    int softDeleteByProgramIdAndPhotographerId(@Param("programId") Long programId,
        @Param("photographerId") Long photographerId);

    List<Program> findByPhotographerIdAndDeletedFalse(Long photographerId);

    Optional<Program> findByIdAndPhotographerId(Long id, Long photographerId);
}
