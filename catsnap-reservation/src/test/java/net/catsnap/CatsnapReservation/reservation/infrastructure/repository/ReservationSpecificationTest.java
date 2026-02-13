package net.catsnap.CatsnapReservation.reservation.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.CatsnapReservation.reservation.domain.CanceledBy;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import net.catsnap.CatsnapReservation.reservation.domain.vo.CancelReason;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot;
import net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture;
import net.catsnap.CatsnapReservation.shared.infrastructure.JpaAuditingConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("ReservationSpecification 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Import(JpaAuditingConfig.class)
@SuppressWarnings("NonAsciiCharacters")
@DataJpaTest
class ReservationSpecificationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private static final java.time.LocalDate TARGET_DATE = ReservationFixture.DEFAULT_TIME_SLOT
        .getReservationDate();

    @AfterEach
    void cleanup() {
        reservationRepository.deleteAll();
    }

    @Test
    void 특정_작가의_특정_날짜_활성_예약만_조회된다() {
        // given
        Reservation target = ReservationFixture.createDefault();
        Reservation otherPhotographer = ReservationFixture.createWithPhotographerId(999L);
        Reservation otherDate = ReservationFixture.createWithTimeSlot(
            new ReservationTimeSlot(
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 1, 12, 0)
            )
        );

        reservationRepository.saveAll(List.of(target, otherPhotographer, otherDate));

        // when
        List<Reservation> result = reservationRepository.findAll(
            ReservationSpecification.activeReservationsOf(
                ReservationFixture.DEFAULT_PHOTOGRAPHER_ID, TARGET_DATE)
        );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getPhotographerId())
            .isEqualTo(ReservationFixture.DEFAULT_PHOTOGRAPHER_ID);
    }

    @Test
    void 취소된_예약은_조회되지_않는다() {
        // given
        Reservation active = ReservationFixture.createDefault();
        Reservation canceled = ReservationFixture.createCanceled();

        reservationRepository.saveAll(List.of(active, canceled));

        // when
        List<Reservation> result = reservationRepository.findAll(
            ReservationSpecification.activeReservationsOf(
                ReservationFixture.DEFAULT_PHOTOGRAPHER_ID, TARGET_DATE)
        );

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 만료된_예약은_조회되지_않는다() {
        // given
        Reservation active = ReservationFixture.createDefault();
        Reservation expired = ReservationFixture.createExpired();

        reservationRepository.saveAll(List.of(active, expired));

        // when
        List<Reservation> result = reservationRepository.findAll(
            ReservationSpecification.activeReservationsOf(
                ReservationFixture.DEFAULT_PHOTOGRAPHER_ID, TARGET_DATE)
        );

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void CONFIRMED_상태_예약도_활성으로_조회된다() {
        // given
        Reservation confirmed = ReservationFixture.createConfirmed();

        reservationRepository.save(confirmed);

        // when
        List<Reservation> result = reservationRepository.findAll(
            ReservationSpecification.activeReservationsOf(
                ReservationFixture.DEFAULT_PHOTOGRAPHER_ID, TARGET_DATE)
        );

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 해당_날짜에_활성_예약이_없으면_빈_목록을_반환한다() {
        // given
        Reservation otherDate = ReservationFixture.createWithTimeSlot(
            new ReservationTimeSlot(
                LocalDateTime.of(2025, 7, 1, 10, 0),
                LocalDateTime.of(2025, 7, 1, 12, 0)
            )
        );
        reservationRepository.save(otherDate);

        // when
        List<Reservation> result = reservationRepository.findAll(
            ReservationSpecification.activeReservationsOf(
                ReservationFixture.DEFAULT_PHOTOGRAPHER_ID, TARGET_DATE)
        );

        // then
        assertThat(result).isEmpty();
    }
}