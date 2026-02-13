package net.catsnap.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import net.catsnap.domain.reservation.document.Holiday;

public record HolidayResponse(
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate holidayDate,
    String holidayName

) {

    public static HolidayResponse from(Holiday holiday) {
        return new HolidayResponse(
            holiday.idToLocalDate(),
            holiday.getHolidayName()
        );
    }
}
