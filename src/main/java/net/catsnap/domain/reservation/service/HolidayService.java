package net.catsnap.domain.reservation.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.reservation.client.HolidayClient;
import net.catsnap.domain.reservation.dto.HolidayListResponse;
import net.catsnap.domain.reservation.dto.HolidayResponse;
import net.catsnap.domain.reservation.repository.HolidayRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayClient holidayClient;

    @Scheduled(cron = "${spring.holiday.schedule.cron}")
    public void saveHolidays() {
        holidayRepository.saveAll(holidayClient.getHolidays());
    }

    public Boolean isHoliday(LocalDate date) {
        return holidayRepository.findById(date.toString()).isPresent();
    }

    public HolidayListResponse getHoliday(YearMonth date) {
        List<HolidayResponse> holidayResponseList = new ArrayList<>();
        holidayRepository.findAll()
            .forEach(holiday -> {
                if (holiday.getId().contains(date.toString())) {
                    holidayResponseList.add(HolidayResponse.from(holiday));
                }
            });
        return new HolidayListResponse(holidayResponseList);
    }
}
