package net.catsnap.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import net.catsnap.domain.reservation.document.ReservationTimeFormat;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;
import net.catsnap.domain.reservation.repository.ReservationTimeFormatRepository;
import net.catsnap.domain.reservation.repository.WeekdayReservationTimeMappingRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.Exception.reservation.NotFoundStartTimeException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationValidatorService {

    private final WeekdayReservationTimeMappingRepository weekdayReservationTimeMappingRepository;
    private final ReservationTimeFormatRepository reservationTimeFormatRepository;
    private final WeekdayService weekdayService;

    /*
     * 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인하는 메소드 입니다.
     */
    public boolean isWithinAllowedDays(LocalDateTime reservationDateTime,
        PhotographerSetting photographerSetting) {
        Long preReservationDays = photographerSetting.getPreReservationDays();
        LocalDate reservationDate = reservationDateTime.toLocalDate();
        return !reservationDate.isAfter(LocalDate.now().plusDays(preReservationDays));
    }

    public boolean isAfterNow(LocalDateTime reservationDateTime) {
        return reservationDateTime.isAfter(LocalDateTime.now());
    }

    /*
     * 해당 일에 사용자가 원하는 예약 시작 시간이 작가의 예약 시간 테이블에 존재하는지 확인하는 메소드 입니다.
     * wantToReservationTime는 HH:mm형식으로 들어옵니다.
     */
    public boolean isValidStartTimeInTimeFormat(LocalDateTime reservationDateTime,
        Long photographerId) {
        Weekday weekday = weekdayService.getWeekday(reservationDateTime.toLocalDate());
        WeekdayReservationTimeMapping weekdayReservationTimeMapping = weekdayReservationTimeMappingRepository.findByPhotographerIdAndWeekday(
                photographerId, weekday)
            .orElseThrow(
                () -> new ResourceNotFoundException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다."));
        String reservationTimeFormatId = weekdayReservationTimeMapping.getReservationTimeFormatId();
        /*
         * 해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않으면 예외 발생
         */
        if (reservationTimeFormatId == null) {
            throw new NotFoundStartTimeException("해당 작가의 해당 요일에 예약 시간 설정이 존재하지 않습니다.");
        }

        ReservationTimeFormat reservationTimeFormat = reservationTimeFormatRepository.findById(
            reservationTimeFormatId);
        List<LocalTime> photographerStartTimeList = reservationTimeFormat.getStartTimeList();

        return photographerStartTimeList.contains(reservationDateTime.toLocalTime());
    }

}
