package com.cuk.catsnap.domain.reservation.repository;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.photographer.repository.PhotographerRepository;
import com.cuk.catsnap.domain.reservation.entity.Weekday;
import com.cuk.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import com.cuk.catsnap.support.fixture.PhotographerFixture;
import com.cuk.catsnap.support.fixture.WeekdayReservationTimeMappingFixture;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class WeekdayReservationTimeMappingRepositoryTest {

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;

    @Test
    void 시간형식_아이디로_매핑된_요일_검색() {
        //given
        Photographer photographer = PhotographerFixture.photographer().build();
        WeekdayReservationTimeMappingDataFixture(photographer);

        //when
        List<WeekdayReservationTimeMapping> weekdayReservationTimeMappings = weekdayReservationTimeMappingRepository.findByPhotographerAndReservationTimeFormatId(
            photographer, "timeFormat1");

        //then
        Assertions.assertThat(weekdayReservationTimeMappings).hasSize(2);
    }

    @Test
    void 요일로_매핑된_시간형식_아이디_검색() {
        //given
        Photographer photographer = PhotographerFixture.photographer().build();
        WeekdayReservationTimeMappingDataFixture(photographer);

        //when
        WeekdayReservationTimeMapping weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
            photographer.getId(), Weekday.MONDAY).orElseThrow();

        //then
        Assertions.assertThat(weekdayReservationTimeMapping.getReservationTimeFormatId())
            .isEqualTo("timeFormat1");
    }

    private void WeekdayReservationTimeMappingDataFixture(Photographer photographer) {
        WeekdayReservationTimeMapping weekdayReservationTimeMapping1 = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
            .photographer(photographer)
            .reservationTimeFormatId("timeFormat1")
            .weekday(Weekday.MONDAY)
            .build();
        WeekdayReservationTimeMapping weekdayReservationTimeMapping2 = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
            .photographer(photographer)
            .reservationTimeFormatId("timeFormat1")
            .weekday(Weekday.FRIDAY)
            .build();
        WeekdayReservationTimeMapping weekdayReservationTimeMapping3 = WeekdayReservationTimeMappingFixture.weekdayReservationTimeMapping()
            .photographer(photographer)
            .reservationTimeFormatId("timeFormat2")
            .weekday(Weekday.SUNDAY)
            .build();
        photographerRepository.save(photographer);
        weekdayReservationTimeMappingRepository.save(weekdayReservationTimeMapping1);
        weekdayReservationTimeMappingRepository.save(weekdayReservationTimeMapping2);
        weekdayReservationTimeMappingRepository.save(weekdayReservationTimeMapping3);
    }
}
/*
    Optional<WeekdayReservationTimeMapping> findByPhotographerIdAndWeekday(Long photographerId,
        Weekday weekday);
 */