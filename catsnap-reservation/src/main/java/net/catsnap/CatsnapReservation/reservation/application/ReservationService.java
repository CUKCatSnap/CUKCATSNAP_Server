package net.catsnap.CatsnapReservation.reservation.application;

import static net.catsnap.CatsnapReservation.reservation.infrastructure.repository.ReservationSpecification.activeReservationsOf;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.program.infrastructure.repository.ProgramRepository;
import net.catsnap.CatsnapReservation.reservation.application.dto.request.ReservationCreateRequest;
import net.catsnap.CatsnapReservation.reservation.application.dto.response.ReservationCreateResponse;
import net.catsnap.CatsnapReservation.reservation.domain.Reservation;
import net.catsnap.CatsnapReservation.reservation.domain.ReservationFactory;
import net.catsnap.CatsnapReservation.reservation.infrastructure.repository.ReservationRepository;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.schedule.infrastructure.repository.PhotographerScheduleRepository;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reservation 애그리거트의 Application Service
 *
 * <p>데이터 조회/저장을 담당하고, 비즈니스 로직은 도메인 서비스({@link ReservationFactory})에 위임합니다.</p>
 */
@Service
public class ReservationService {

    private static final long HOLD_MINUTES = 15;

    private final ReservationRepository reservationRepository;
    private final ProgramRepository programRepository;
    private final PhotographerScheduleRepository photographerScheduleRepository;
    private final ReservationFactory reservationFactory;
    private final Clock clock;

    public ReservationService(
        ReservationRepository reservationRepository,
        ProgramRepository programRepository,
        PhotographerScheduleRepository photographerScheduleRepository,
        Clock clock
    ) {
        this.reservationRepository = reservationRepository;
        this.programRepository = programRepository;
        this.photographerScheduleRepository = photographerScheduleRepository;
        this.reservationFactory = new ReservationFactory();
        this.clock = clock;
    }

    @Transactional
    public ReservationCreateResponse createReservation(Long modelId, ReservationCreateRequest request) {
        // 1. 애그리거트 조회
        Program program = programRepository.findById(request.programId())
            .orElseThrow(() -> new DomainException(DomainErrorCode.NOT_FOUND, "프로그램을 찾을 수 없습니다."));

        PhotographerSchedule schedule = photographerScheduleRepository.findByPhotographerId(
                program.getPhotographerId())
            .orElseThrow(() -> new DomainException(DomainErrorCode.NOT_FOUND, "작가의 스케줄을 찾을 수 없습니다."));

        List<Reservation> activeReservations = reservationRepository.findAll(
            activeReservationsOf(program.getPhotographerId(), request.reservationDate())
        );

        // 2. 도메인 서비스에 위임 (검증 + 생성)
        LocalDate today = LocalDate.now(clock);
        LocalDateTime holdExpiresAt = LocalDateTime.now(clock).plusMinutes(HOLD_MINUTES);

        Reservation reservation = reservationFactory.create(
            modelId,
            program,
            schedule,
            activeReservations,
            request.reservationDate(),
            request.startTime(),
            holdExpiresAt,
            today
        );

        // 3. 저장 및 응답
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationCreateResponse(
            saved.getReservationNumber().getValue(),
            saved.getAmount().getValue(),
            saved.getHoldExpiresAt()
        );
    }
}