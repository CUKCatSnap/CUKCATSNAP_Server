package net.catsnap.domain.reservation.repository;

import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.support.fixture.ReservationTimeFormatFixture;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

@DataMongoTest
@Import(ReservationTimeFormatRepository.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationTimeFormatRepositoryTest {

    @Autowired
    private ReservationTimeFormatRepository reservationTimeFormatRepository;

    @Test
    void 예약_시간_형식_저장() {
        //given
        ReservationTimeFormat reservationTimeFormat1 = ReservationTimeFormatFixture.reservationTimeFormat()
            .build();
        ReservationTimeFormat reservationTimeFormat2 = ReservationTimeFormatFixture.reservationTimeFormat()
            .startTimeList(List.of(LocalTime.of(12, 0), LocalTime.of(13, 0)))
            .build();

        //when
        reservationTimeFormatRepository.save(reservationTimeFormat1);
        reservationTimeFormatRepository.save(reservationTimeFormat2);

        //then
        List<ReservationTimeFormat> reservationTimeFormats = reservationTimeFormatRepository.findByPhotographerId(
            reservationTimeFormat1.getPhotographerId());
        Assertions.assertThat(reservationTimeFormats).hasSize(2);

        //clean
        reservationTimeFormatRepository.deleteById(reservationTimeFormat1.getId(),
            reservationTimeFormat1.getPhotographerId());
        reservationTimeFormatRepository.deleteById(reservationTimeFormat2.getId(),
            reservationTimeFormat2.getPhotographerId());
    }

    @Test
    void 예약_시간_형식_수정() {
        //given
        ReservationTimeFormat reservationTimeFormat = ReservationTimeFormatFixture.reservationTimeFormat()
            .formatName("test")
            .startTimeList(List.of(LocalTime.of(10, 0), LocalTime.of(11, 0)))
            .build();
        reservationTimeFormatRepository.save(reservationTimeFormat);

        //when
        ReservationTimeFormat updatedReservationTimeFormat = ReservationTimeFormatFixture.reservationTimeFormat()
            .id(reservationTimeFormat.getId())
            .photographerId(reservationTimeFormat.getPhotographerId())
            .formatName("updatedFormatName")
            .startTimeList(List.of(LocalTime.of(12, 0), LocalTime.of(13, 0)))
            .build();
        reservationTimeFormatRepository.update(updatedReservationTimeFormat);

        //then
        ReservationTimeFormat findReservationTimeFormat = reservationTimeFormatRepository.findById(
            reservationTimeFormat.getId());
        Assertions.assertThat(findReservationTimeFormat.getFormatName())
            .isEqualTo("updatedFormatName");
        Assertions.assertThat(findReservationTimeFormat.getStartTimeList())
            .isEqualTo(List.of(LocalTime.of(12, 0), LocalTime.of(13, 0)));

        //clean
        reservationTimeFormatRepository.deleteById(reservationTimeFormat.getId(),
            reservationTimeFormat.getPhotographerId());
    }
}
