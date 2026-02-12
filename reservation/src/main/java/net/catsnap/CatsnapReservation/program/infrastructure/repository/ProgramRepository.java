package net.catsnap.CatsnapReservation.program.infrastructure.repository;

import net.catsnap.CatsnapReservation.program.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 프로그램 Repository
 */
public interface ProgramRepository extends JpaRepository<Program, Long>,
    JpaSpecificationExecutor<Program> {
}
