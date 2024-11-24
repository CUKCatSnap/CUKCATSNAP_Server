package net.catsnap.support.fixture;

import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.reservation.entity.Weekday;
import net.catsnap.domain.reservation.entity.WeekdayReservationTimeMapping;

public class WeekdayReservationTimeMappingFixture {

    private Long id;
    private Photographer photographer = PhotographerFixture.photographer().build();
    private String reservationTimeFormatId = "reservationTimeFormatId";
    private Weekday weekday = Weekday.MONDAY;

    public static WeekdayReservationTimeMappingFixture weekdayReservationTimeMapping() {
        return new WeekdayReservationTimeMappingFixture();
    }

    public WeekdayReservationTimeMappingFixture photographer(Photographer photographer) {
        this.photographer = photographer;
        return this;
    }

    public WeekdayReservationTimeMappingFixture reservationTimeFormatId(
        String reservationTimeFormatId) {
        this.reservationTimeFormatId = reservationTimeFormatId;
        return this;
    }

    public WeekdayReservationTimeMappingFixture weekday(Weekday weekday) {
        this.weekday = weekday;
        return this;
    }

    public WeekdayReservationTimeMapping build() {
        return WeekdayReservationTimeMapping.builder()
            .id(this.id)
            .photographer(this.photographer)
            .reservationTimeFormatId(this.reservationTimeFormatId)
            .weekday(this.weekday)
            .build();
    }
}
