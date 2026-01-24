package net.catsnap.CatsnapReservation.schedule.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.infrastructure.repository.PhotographerScheduleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사진작가 스케줄 관리 서비스
 *
 * <p>사진작가의 예약 가능 스케줄을 생성하고 관리합니다.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PhotographerScheduleService {

    private final PhotographerScheduleRepository photographerScheduleRepository;

    /**
     * 사진작가의 기본 스케줄을 생성합니다.
     *
     * <p>이미 스케줄이 존재하면 아무 작업도 수행하지 않습니다 (멱등성 보장).
     * 동시 요청으로 인한 중복 생성 시도 시 DataIntegrityViolationException을 catch하여 정상 처리합니다.</p>
     *
     * @param photographerId 사진작가 ID
     */
    @Transactional
    public void createDefaultSchedule(Long photographerId) {
        if (photographerScheduleRepository.existsByPhotographerId(photographerId)) {
            return;
        }

        try {
            PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
            photographerScheduleRepository.saveAndFlush(schedule);
            log.info("Schedule created: photographerId={}", photographerId);
        } catch (DataIntegrityViolationException e) {
            log.info("Schedule already created by concurrent request: photographerId={}", photographerId);
        }
    }
}
