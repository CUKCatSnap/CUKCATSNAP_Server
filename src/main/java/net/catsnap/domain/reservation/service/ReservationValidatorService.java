package net.catsnap.domain.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.photographer.document.PhotographerSetting;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationValidatorService {

    /*
     * 해당 작가가 해당 일에 예약을 받을 수 있게 했는지 확인하는 메소드 입니다.
     */
    public boolean isWithinAllowedDays(LocalDateTime reservationDateTime,
        PhotographerSetting photographerSetting) {
        Long preReservationDays = photographerSetting.getPreReservationDays();
        LocalDate reservationDate = reservationDateTime.toLocalDate();
        return !reservationDate.isAfter(LocalDate.now().plusDays(preReservationDays));
    }
}
