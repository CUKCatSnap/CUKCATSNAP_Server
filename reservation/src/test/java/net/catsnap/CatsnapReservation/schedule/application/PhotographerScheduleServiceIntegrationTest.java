package net.catsnap.CatsnapReservation.schedule.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.fixture.PhotographerScheduleFixture;
import net.catsnap.CatsnapReservation.schedule.infrastructure.repository.PhotographerScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("PhotographerScheduleService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class PhotographerScheduleServiceIntegrationTest {

    @Autowired
    private PhotographerScheduleService photographerScheduleService;

    @Autowired
    private PhotographerScheduleRepository photographerScheduleRepository;

    @BeforeEach
    void setUp() {
        photographerScheduleRepository.deleteAll();
    }

    @Test
    void 새로운_photographerId로_기본_스케줄을_생성한다() {
        // given
        Long photographerId = 1L;

        // when
        photographerScheduleService.createDefaultSchedule(photographerId);

        // then
        Optional<PhotographerSchedule> found = photographerScheduleRepository.findByPhotographerId(photographerId);
        assertThat(found).isPresent();
        assertThat(found.get().getPhotographerId()).isEqualTo(photographerId);
        assertThat(found.get().getWeekdayRules()).hasSize(7);
    }

    @Test
    void 이미_스케줄이_존재하면_중복_생성하지_않는다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule existingSchedule = PhotographerScheduleFixture.createWithPhotographerId(photographerId);
        photographerScheduleRepository.save(existingSchedule);
        Long originalId = existingSchedule.getId();

        // when
        photographerScheduleService.createDefaultSchedule(photographerId);

        // then
        Optional<PhotographerSchedule> found = photographerScheduleRepository.findByPhotographerId(photographerId);
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(originalId);
        assertThat(photographerScheduleRepository.count()).isEqualTo(1);
    }

    @Test
    void 동일한_photographerId로_여러번_호출해도_스케줄은_하나만_존재한다() {
        // given
        Long photographerId = 1L;

        // when
        photographerScheduleService.createDefaultSchedule(photographerId);
        photographerScheduleService.createDefaultSchedule(photographerId);
        photographerScheduleService.createDefaultSchedule(photographerId);

        // then
        assertThat(photographerScheduleRepository.count()).isEqualTo(1);
    }

    @Test
    void 서로_다른_photographerId로_호출하면_각각_스케줄이_생성된다() {
        // given
        Long photographerId1 = 1L;
        Long photographerId2 = 2L;
        Long photographerId3 = 3L;

        // when
        photographerScheduleService.createDefaultSchedule(photographerId1);
        photographerScheduleService.createDefaultSchedule(photographerId2);
        photographerScheduleService.createDefaultSchedule(photographerId3);

        // then
        assertThat(photographerScheduleRepository.count()).isEqualTo(3);
        assertThat(photographerScheduleRepository.findByPhotographerId(photographerId1)).isPresent();
        assertThat(photographerScheduleRepository.findByPhotographerId(photographerId2)).isPresent();
        assertThat(photographerScheduleRepository.findByPhotographerId(photographerId3)).isPresent();
    }
}
