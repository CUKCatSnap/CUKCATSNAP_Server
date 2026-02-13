package net.catsnap.CatsnapReservation.reservation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.fixture.ProgramFixture;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot;
import net.catsnap.CatsnapReservation.reservation.fixture.ReservationFixture;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.domain.vo.AvailableStartTimes;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ReservationFactory 도메인 서비스 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationFactoryTest {

    private ReservationFactory reservationFactory;

    private static final Long MODEL_ID = 1L;
    private static final Long PHOTOGRAPHER_ID = 1L;
    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate RESERVATION_DATE = TODAY.plusDays(1);
    private static final LocalTime START_TIME = LocalTime.of(10, 0);
    private static final LocalDateTime HOLD_EXPIRES_AT = RESERVATION_DATE.atTime(10, 15);

    @BeforeEach
    void setUp() {
        reservationFactory = new ReservationFactory();
    }

    private static final Long PROGRAM_ID = 100L;

    private Program createProgram() {
        return ProgramFixture.createDefaultWithId(PROGRAM_ID);
    }

    private PhotographerSchedule createScheduleWith(LocalTime... times) {
        PhotographerSchedule schedule = PhotographerSchedule.initSchedule(PHOTOGRAPHER_ID);
        DayOfWeek dayOfWeek = RESERVATION_DATE.getDayOfWeek();
        schedule.updateWeekdayRule(dayOfWeek, AvailableStartTimes.of(List.of(times)));
        return schedule;
    }

    @Nested
    class 예약_생성_성공 {

        @Test
        void 정상적으로_예약을_생성한다() {
            // given
            Program program = createProgram();
            PhotographerSchedule schedule = createScheduleWith(START_TIME);

            // when
            Reservation reservation = reservationFactory.create(
                MODEL_ID, program, schedule, Collections.emptyList(),
                RESERVATION_DATE, START_TIME, HOLD_EXPIRES_AT, TODAY
            );

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
            assertThat(reservation.getModelId()).isEqualTo(MODEL_ID);
            assertThat(reservation.getPhotographerId()).isEqualTo(PHOTOGRAPHER_ID);
            assertThat(reservation.getReservationNumber()).isNotNull();
            assertThat(reservation.getTimeSlot().getStartDateTime())
                .isEqualTo(RESERVATION_DATE.atTime(START_TIME));
            assertThat(reservation.getTimeSlot().getEndDateTime())
                .isEqualTo(RESERVATION_DATE.atTime(START_TIME).plusMinutes(60));
            assertThat(reservation.getAmount().getValue()).isEqualTo(100000L);
            assertThat(reservation.getHoldExpiresAt()).isEqualTo(HOLD_EXPIRES_AT);
        }

        @Test
        void 겹치지_않는_기존_예약이_있으면_생성에_성공한다() {
            // given
            Program program = createProgram();
            PhotographerSchedule schedule = createScheduleWith(START_TIME, LocalTime.of(11, 0));

            // 11:00~12:00 기존 예약 (10:00~11:00과 겹치지 않음)
            Reservation existing = Reservation.hold(
                2L, PHOTOGRAPHER_ID, 3L,
                new ReservationTimeSlot(
                    RESERVATION_DATE.atTime(11, 0),
                    RESERVATION_DATE.atTime(12, 0)),
                ReservationFixture.DEFAULT_AMOUNT,
                HOLD_EXPIRES_AT
            );

            // when
            Reservation reservation = reservationFactory.create(
                MODEL_ID, program, schedule, List.of(existing),
                RESERVATION_DATE, START_TIME, HOLD_EXPIRES_AT, TODAY
            );

            // then
            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.PENDING);
        }
    }

    @Nested
    class 예약_생성_실패 {

        @Test
        void 삭제된_프로그램이면_예외가_발생한다() {
            // given
            Program program = ProgramFixture.createDeletedWithId(PROGRAM_ID);
            PhotographerSchedule schedule = createScheduleWith(START_TIME);

            // when & then
            assertThatThrownBy(() -> reservationFactory.create(
                MODEL_ID, program, schedule, Collections.emptyList(),
                RESERVATION_DATE, START_TIME, HOLD_EXPIRES_AT, TODAY
            ))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("삭제된 프로그램은 예약할 수 없습니다");
        }

        @Test
        void 예약_불가능한_시간대이면_예외가_발생한다() {
            // given
            Program program = createProgram();
            PhotographerSchedule schedule = createScheduleWith(LocalTime.of(14, 0));

            // when & then
            assertThatThrownBy(() -> reservationFactory.create(
                MODEL_ID, program, schedule, Collections.emptyList(),
                RESERVATION_DATE, START_TIME, HOLD_EXPIRES_AT, TODAY
            ))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("해당 시간대는 예약할 수 없습니다");
        }

        @Test
        void 기존_예약과_시간이_겹치면_예외가_발생한다() {
            // given
            Program program = createProgram();
            PhotographerSchedule schedule = createScheduleWith(START_TIME);

            // 10:00~11:00 기존 예약 (겹침)
            Reservation existing = Reservation.hold(
                2L, PHOTOGRAPHER_ID, 3L,
                new ReservationTimeSlot(
                    RESERVATION_DATE.atTime(10, 0),
                    RESERVATION_DATE.atTime(11, 0)),
                ReservationFixture.DEFAULT_AMOUNT,
                HOLD_EXPIRES_AT
            );

            // when & then
            assertThatThrownBy(() -> reservationFactory.create(
                MODEL_ID, program, schedule, List.of(existing),
                RESERVATION_DATE, START_TIME, HOLD_EXPIRES_AT, TODAY
            ))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("해당 시간대에 이미 예약이 존재합니다");
        }
    }
}