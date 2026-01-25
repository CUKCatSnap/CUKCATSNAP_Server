package net.catsnap.CatsnapReservation.program.infrastructure.repository;

import net.catsnap.CatsnapReservation.program.domain.Program;
import org.springframework.data.jpa.domain.Specification;

/**
 * 프로그램 조회 Specification
 * <p>
 * 도메인 언어로 조회 조건을 표현합니다.
 */
public class ProgramSpecification {

    private ProgramSpecification() {
    }

    /**
     * 활성 상태인 프로그램 (삭제되지 않은)
     * <p>
     * 예약 목록 조회 시 사용
     */
    public static Specification<Program> isActive() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    /**
     * 삭제된 프로그램
     */
    public static Specification<Program> isDeleted() {
        return (root, query, cb) -> cb.isNotNull(root.get("deletedAt"));
    }

    /**
     * 특정 작가의 프로그램
     *
     * @param photographerId 작가 ID
     */
    public static Specification<Program> belongsTo(Long photographerId) {
        return (root, query, cb) -> cb.equal(root.get("photographerId"), photographerId);
    }
}
