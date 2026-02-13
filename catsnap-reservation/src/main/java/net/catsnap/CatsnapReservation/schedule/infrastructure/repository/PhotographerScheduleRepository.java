package net.catsnap.CatsnapReservation.schedule.infrastructure.repository;

import java.util.Optional;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사진작가 스케줄 Repository
 */
public interface PhotographerScheduleRepository extends JpaRepository<PhotographerSchedule, Long> {

    /**
     * 사진작가 ID로 스케줄을 조회합니다.
     *
     * @param photographerId 사진작가 ID
     * @return 스케줄 (존재하지 않으면 empty)
     */
    Optional<PhotographerSchedule> findByPhotographerId(Long photographerId);

    /**
     * 사진작가 ID로 스케줄 존재 여부를 확인합니다.
     *
     * @param photographerId 사진작가 ID
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByPhotographerId(Long photographerId);
}