package net.catsnap.domain.reservation.service;

import net.catsnap.domain.reservation.entity.Weekday;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeekdayService {

    private final HolidayService holidayService;

    public Weekday getWeekday(LocalDate date) {
        if (holidayService.isHoliday(date)) {
            return Weekday.HOLIDAY;
        } else {
            return Weekday.valueOf(date.getDayOfWeek().name());
        }
    }
}
