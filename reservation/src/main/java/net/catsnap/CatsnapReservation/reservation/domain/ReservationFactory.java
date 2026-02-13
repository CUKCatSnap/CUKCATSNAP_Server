package net.catsnap.CatsnapReservation.reservation.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import net.catsnap.CatsnapReservation.program.domain.Program;
import net.catsnap.CatsnapReservation.reservation.domain.vo.Money;
import net.catsnap.CatsnapReservation.reservation.domain.vo.ReservationTimeSlot;
import net.catsnap.CatsnapReservation.schedule.domain.PhotographerSchedule;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainErrorCode;
import net.catsnap.CatsnapReservation.shared.domain.error.DomainException;

/**
 * 예약 생성 도메인 서비스 (Factory)
 *
 * <p>여러 애그리거트(Program, PhotographerSchedule, 기존 Reservation)를 조율하여
 * 예약 가능 여부를 검증하고 새로운 예약을 생성합니다.</p>
 *
 * <p>순수 도메인 객체로, 인프라스트럭처(리포지토리 등)에 의존하지 않습니다.
 * 필요한 데이터는 애플리케이션 서비스가 로드하여 전달합니다.</p>
 */
public class ReservationFactory {

    /**
     * 예약을 생성합니다.
     *
     * <p>다음 순서로 도메인 규칙을 검증합니다:</p>
     * <ol>
     *   <li>프로그램 예약 가능 상태 검증</li>
     *   <li>작가 스케줄 가용성 검증</li>
     *   <li>예약 시간대 생성 및 기존 예약과 겹침 검증</li>
     *   <li>예약 생성</li>
     * </ol>
     *
     * @param modelId              예약 요청 모델(고객) ID
     * @param program              예약 대상 프로그램
     * @param schedule             작가의 예약 가능 스케줄
     * @param existingReservations 해당 날짜의 기존 활성 예약 목록
     * @param reservationDate      예약 날짜
     * @param startTime            예약 시작 시간
     * @param holdExpiresAt        임시 점유 만료 시각
     * @return PENDING 상태의 새 예약
     * @throws DomainException 예약 불가능한 경우
     */
    public Reservation create(
        Long modelId,
        Program program,
        PhotographerSchedule schedule,
        List<Reservation> existingReservations,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalDateTime holdExpiresAt
    ) {
        // 1. 프로그램 예약 가능 상태 검증
        program.ensureBookable();

        // 2. 작가 스케줄 가용성 검증
        schedule.ensureAvailable(reservationDate, startTime);

        // 3. 시간대 생성 및 겹침 검증
        LocalDateTime startDateTime = reservationDate.atTime(startTime);
        LocalDateTime endDateTime = program.getDuration().addTo(startDateTime);
        ReservationTimeSlot timeSlot = new ReservationTimeSlot(startDateTime, endDateTime);

        boolean hasOverlap = existingReservations.stream()
            .anyMatch(existing -> existing.getTimeSlot().overlaps(timeSlot));

        if (hasOverlap) {
            throw new DomainException(DomainErrorCode.CONFLICT, "해당 시간대에 이미 예약이 존재합니다.");
        }

        // 4. 예약 생성
        Money amount = new Money(program.getPrice().getValue());

        return Reservation.hold(
            modelId,
            program.getPhotographerId(),
            program.getId(),
            timeSlot,
            amount,
            holdExpiresAt
        );
    }
}