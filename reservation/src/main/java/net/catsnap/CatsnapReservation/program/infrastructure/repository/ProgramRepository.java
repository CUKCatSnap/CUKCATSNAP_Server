package net.catsnap.CatsnapReservation.program.infrastructure.repository;

import java.util.Optional;
import net.catsnap.CatsnapReservation.program.domain.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 프로그램 Repository
 */
public interface ProgramRepository extends JpaRepository<Program, Long>,
    JpaSpecificationExecutor<Program> {

    /**
     * 프로그램 ID로 조회 (삭제 여부 상관없이)
     * <p>
     * 리뷰 조회 등 삭제된 프로그램도 보여야 하는 경우 사용
     *
     * @param id 프로그램 ID
     * @return 프로그램 (존재하지 않으면 empty)
     */
    Optional<Program> findById(Long id);
}
