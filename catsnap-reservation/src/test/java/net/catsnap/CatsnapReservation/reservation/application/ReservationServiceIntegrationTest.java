package net.catsnap.CatsnapReservation.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.fixture.ProgramFixture;
import net.catsnap.CatsnapReservation.program.infrastructure.repository.ProgramRepository;
import net.catsnap.CatsnapReservation.reservation.application.dto.request.ReservationCreateRequest;
import net.catsnap.CatsnapReservation.reservation.application.dto.response.ReservationCreateResponse;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import net.catsnap.CatsnapReservation.reservation.domain.ReservationStatus;
import net.catsnap.CatsnapReservation.reservation.infrastructure.repository.ReservationRepository;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import net.catsnap.CatsnapReservation.schedule.infrastructure.repository.PhotographerScheduleRepository;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("ReservationService 통합 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationServiceIntegrationTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private PhotographerScheduleRepository photographerScheduleRepository;

    private static final Long MODEL_ID = 1L;
    private static final Long PHOTOGRAPHER_ID = 2L;

    @AfterEach
    void cleanup() {
        reservationRepository.deleteAll();
        programRepository.deleteAll();
        photographerScheduleRepository.deleteAll();
    }

    private Program saveProgram() {
        Program program = ProgramFixture.createWithPhotographerId(PHOTOGRAPHER_ID);
        return programRepository.save(program);
    }

    private PhotographerSchedule saveSchedule(LocalDate date, LocalTime... times) {
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(PHOTOGRAPHER_ID);
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        schedule.updateWeekdayRule(dayOfWeek, AvailableStartTimes.of(List.of(times)));
        return photographerScheduleRepository.save(schedule);
    }

    @Nested
    class 예약_생성_성공 {

        @Test
        @Transactional
        void 정상적으로_예약을_생성한다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0), LocalTime.of(11, 0));

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(10, 0));

            // when
            ReservationCreateResponse response = reservationService.createReservation(MODEL_ID, request);

            // then
            assertThat(response.reservationNumber()).isNotNull();
            assertThat(response.amount()).isEqualTo(100000L);
            assertThat(response.holdExpiresAt()).isNotNull();
        }

        @Test
        @Transactional
        void 생성된_예약이_DB에_PENDING_상태로_저장된다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0));

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(10, 0));

            // when
            ReservationCreateResponse response = reservationService.createReservation(MODEL_ID, request);

            // then
            List<Reservation> reservations = reservationRepository.findAll();
            assertThat(reservations).hasSize(1);

            Reservation saved = reservations.get(0);
            assertThat(saved.getStatus()).isEqualTo(ReservationStatus.PENDING);
            assertThat(saved.getModelId()).isEqualTo(MODEL_ID);
            assertThat(saved.getPhotographerId()).isEqualTo(PHOTOGRAPHER_ID);
            assertThat(saved.getTimeSlot().getStartDateTime())
                .isEqualTo(date.atTime(10, 0));
            assertThat(saved.getTimeSlot().getEndDateTime())
                .isEqualTo(date.atTime(11, 0));
        }

        @Test
        @Transactional
        void 같은_작가의_겹치지_않는_시간대는_예약할_수_있다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0), LocalTime.of(11, 0));

            ReservationCreateRequest request1 = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(10, 0));
            ReservationCreateRequest request2 = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(11, 0));

            // when
            reservationService.createReservation(MODEL_ID, request1);
            reservationService.createReservation(MODEL_ID, request2);

            // then
            assertThat(reservationRepository.findAll()).hasSize(2);
        }
    }

    @Nested
    class 예약_생성_실패 {

        @Test
        void 프로그램이_존재하지_않으면_예외가_발생한다() {
            // given
            ReservationCreateRequest request = new ReservationCreateRequest(
                999L, LocalDate.now().plusDays(1), LocalTime.of(10, 0));

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(MODEL_ID, request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("프로그램을 찾을 수 없습니다");
        }

        @Test
        @Transactional
        void 삭제된_프로그램이면_예외가_발생한다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0));

            program.delete(LocalDateTime.now());
            programRepository.save(program);

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(10, 0));

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(MODEL_ID, request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("삭제된 프로그램");
        }

        @Test
        @Transactional
        void 작가_스케줄이_없으면_예외가_발생한다() {
            // given
            Program program = saveProgram();

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), LocalDate.now().plusDays(1), LocalTime.of(10, 0));

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(MODEL_ID, request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("스케줄을 찾을 수 없습니다");
        }

        @Test
        @Transactional
        void 예약_불가능한_시간대이면_예외가_발생한다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0));

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(14, 0));

            // when & then
            assertThatThrownBy(() -> reservationService.createReservation(MODEL_ID, request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("해당 시간대는 예약할 수 없습니다");
        }

        @Test
        @Transactional
        void 기존_예약과_시간이_겹치면_예외가_발생한다() {
            // given
            Program program = saveProgram();
            LocalDate date = LocalDate.now().plusDays(1);
            saveSchedule(date, LocalTime.of(10, 0));

            ReservationCreateRequest request = new ReservationCreateRequest(
                program.getId(), date, LocalTime.of(10, 0));

            // 첫 번째 예약 성공
            reservationService.createReservation(MODEL_ID, request);

            // when & then - 같은 시간대 두 번째 예약 실패
            assertThatThrownBy(() -> reservationService.createReservation(MODEL_ID, request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("이미 예약이 존재합니다");
        }
    }
}