package net.catsnap.support.fixture;

import java.time.LocalDate;
import net.catsnap.domain.reservation.document.Holiday;

public class HolidayFixture {

    private String id = "2024-12-25";
    private String holidayName = "크리스마스";

    public static HolidayFixture Holiday() {
        return new HolidayFixture();
    }

    public HolidayFixture id(LocalDate id) {
        this.id = id.toString();
        return this;
    }

    public HolidayFixture holidayName(String holidayName) {
        this.holidayName = holidayName;
        return this;
    }

    public Holiday build() {
        return new Holiday(LocalDate.parse(id), holidayName);
    }
}
