package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.entity.Weekday;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class WeekdayService {

    /*
     * todo : 공휴일을 체크하는 로직이 없음. 공휴일을 체크하는 로직을 추가해야함.
     */
    public Weekday getWeekday(LocalDate date) {
        return Weekday.valueOf(date.getDayOfWeek().name());
    }
}
