package com.cuk.catsnap.domain.reservation.service;

import com.cuk.catsnap.domain.reservation.client.HolidayClient;
import com.cuk.catsnap.domain.reservation.document.Holiday;
import com.cuk.catsnap.domain.reservation.repository.HolidayRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayClient holidayClient;
    @Value("${spring.env}")
    String env;

    @Scheduled(cron = "${spring.holiday.schedule.cron}")
    public void saveHolidays() {
        List<LocalDate> holidays = holidayClient.getHolidays();
        holidays.forEach(holiday -> {
            holidayRepository.save(new Holiday(holiday));
        });
    }

    @PostConstruct
    public void init(
    ) {
        if (!env.equals("test")) {
            saveHolidays();
        }
    }
}
