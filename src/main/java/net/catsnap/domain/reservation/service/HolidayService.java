package net.catsnap.domain.reservation.service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.client.HolidayClient;
import net.catsnap.domain.reservation.document.Holiday;
import net.catsnap.domain.reservation.repository.HolidayRepository;
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

    public Boolean isHoliday(LocalDate date) {
        return holidayRepository.findById(date.toString()).isPresent();
    }

    @PostConstruct
    public void init(
    ) {
        if (!env.equals("test")) {
            try {
                saveHolidays();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
