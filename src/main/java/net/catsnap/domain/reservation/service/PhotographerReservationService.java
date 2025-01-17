package net.catsnap.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.reservation.dto.MonthReservationCheckListResponse;
import net.catsnap.domain.reservation.dto.MonthReservationCheckResponse;
import net.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationListResponse;
import net.catsnap.domain.reservation.dto.photographer.response.PhotographerReservationInformationResponse;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.entity.ReservationState;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import net.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.Exception.reservation.CanNotChangeReservationState;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhotographerReservationService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final ReservationRepository reservationRepository;

    /*
     * 새로운 작가가 회원가입을 하면, 각 요일에 대한 예약 테이블을 생성한다.
     * 작가는 예약 시간 형식을 만들 수 있고, 이 형식을 특정 요일에 매핑시켜야 한다.
     * 따라서 작가의 예약 시간 형식을 요일에 매핑시키기 위해 요일 - 형식 테이블을 생성한다.
     */
    public void createJoinedPhotographerReservationTimeFormat(Photographer photographer) {
        //추후 벌크 삽입으로 변경 예정
        List<WeekdayReservationTimeMapping> weekdayReservationTimeMappingList
            = Arrays.stream(Weekday.values())
            .map(weekday -> WeekdayReservationTimeMapping.builder()
                .photographer(photographer)
                .weekday(weekday)
                .build()
            )
            .toList();
        weekdayReservationTimeMappingRepository.saveAll(weekdayReservationTimeMappingList);
    }

    public MonthReservationCheckListResponse getReservationListByMonth(LocalDate month) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        LocalDateTime startOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(), 1, 0,
            0, 0);
        LocalDateTime endOfMonth = LocalDateTime.of(month.getYear(), month.getMonthValue(),
            month.lengthOfMonth(), 23, 59, 59);

        List<Reservation> reservationList = reservationRepository.findAllReservationByPhotographerIdAndStartTimeBetween(
            photographerId, startOfMonth, endOfMonth);

        return MonthReservationCheckListResponse.from(
            reservationList.stream()
                .map(MonthReservationCheckResponse::from)
                .toList()
        );
    }

    public PhotographerReservationInformationListResponse getReservationDetailListByDay(
        LocalDate day) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        LocalDateTime startOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 0, 0, 0);
        LocalDateTime endOfDay = LocalDateTime.of(day.getYear(), day.getMonthValue(),
            day.getDayOfMonth(), 23, 59, 59);

        List<Reservation> reservationList = reservationRepository.findAllReservationWithEagerByPhotographerIdAndStartTimeBetween(
            photographerId, startOfDay, endOfDay);

        return PhotographerReservationInformationListResponse.from(
            reservationList.stream()
                .map(PhotographerReservationInformationResponse::from)
                .toList());
    }

    /*
     * 예약 상태를 변경한다.
     * 작가가 예약을 바꿀 수 있는 경우는 아래와 같다.
     * (PENDING -> APPROVED) (PENDING -> REJECTED) (APPROVED -> PHOTOGRAPHY_CANCELLED)
     */
    public void changeReservationState(Long reservationId, ReservationState reservationState) {
        Long photographerId = GetAuthenticationInfo.getUserId();
        reservationRepository.findById(reservationId)
            .ifPresentOrElse(reservation -> {
                reservation.checkPhotographerOwnership(photographerId);
                if (isPossibleChangeReservationState(reservation, reservationState)) {
                    reservation.setReservationState(reservationState);
                } else {
                    throw new CanNotChangeReservationState("예약 상태를 변경할 수 없습니다.");
                }
            }, () -> {
                throw new ResourceNotFoundException("해당 예약 정보를 찾을 수 없습니다.");
            });
    }

    private boolean isPossibleChangeReservationState(Reservation currentReservation,
        ReservationState targetReservationState) {
        return switch (currentReservation.getReservationState()) {
            case PENDING -> targetReservationState == ReservationState.APPROVED
                || targetReservationState == ReservationState.REJECTED;
            case APPROVED -> targetReservationState == ReservationState.PHOTOGRAPHY_CANCELLED;
            default -> false;
        };
    }
}
