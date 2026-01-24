package net.catsnap.CatsnapReservation.schedule.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("PhotographerScheduleRepository 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class PhotographerScheduleRepositoryTest {

    @Autowired
    private PhotographerScheduleRepository photographerScheduleRepository;

    @Test
    void photographerId로_스케줄을_조회할_수_있다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        photographerScheduleRepository.save(schedule);

        // when
        Optional<PhotographerSchedule> found = photographerScheduleRepository.findByPhotographerId(photographerId);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPhotographerId()).isEqualTo(photographerId);
    }

    @Test
    void 존재하지_않는_photographerId로_조회하면_빈_Optional을_반환한다() {
        // given
        Long nonExistentPhotographerId = 999L;

        // when
        Optional<PhotographerSchedule> found = photographerScheduleRepository.findByPhotographerId(nonExistentPhotographerId);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    void photographerId로_스케줄_존재_여부를_확인할_수_있다() {
        // given
        Long photographerId = 1L;
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(photographerId);
        photographerScheduleRepository.save(schedule);

        // when
        boolean exists = photographerScheduleRepository.existsByPhotographerId(photographerId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 존재하지_않는_photographerId로_존재_여부를_확인하면_false를_반환한다() {
        // given
        Long nonExistentPhotographerId = 999L;

        // when
        boolean exists = photographerScheduleRepository.existsByPhotographerId(nonExistentPhotographerId);

        // then
        assertThat(exists).isFalse();
    }
}
