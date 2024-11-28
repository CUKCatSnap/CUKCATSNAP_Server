package net.catsnap.support.fixture;

import net.catsnap.domain.photographer.document.PhotographerSetting;

public class PhotographerSettingFixture {

    private String id;
    private Long photographerId;
    private Boolean autoReservationAccept = true;
    private Boolean enableOverBooking = false;
    private Long preReservationDays = 14L;

    public static PhotographerSettingFixture photographerSetting() {
        return new PhotographerSettingFixture();
    }

    public PhotographerSettingFixture preReservationDays(Long preReservationDays) {
        this.preReservationDays = preReservationDays;
        return this;
    }

    public PhotographerSetting build() {
        return PhotographerSetting.builder()
            .id(this.id)
            .photographerId(this.photographerId)
            .autoReservationAccept(this.autoReservationAccept)
            .enableOverBooking(this.enableOverBooking)
            .preReservationDays(this.preReservationDays)
            .build();
    }
}
