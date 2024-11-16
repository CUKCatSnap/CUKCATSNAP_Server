package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.reservation.document.ReservationTimeFormat;
import java.time.LocalTime;
import java.util.List;

public class ReservationTimeFormatFixture {

    private String id;
    private Long photographerId = 1L;
    private String formatName = "test";
    private List<LocalTime> startTimeList = List.of(LocalTime.of(10, 0), LocalTime.of(11, 0));

    public static ReservationTimeFormatFixture reservationTimeFormat() {
        return new ReservationTimeFormatFixture();
    }

    public ReservationTimeFormatFixture id(String id) {
        this.id = id;
        return this;
    }

    public ReservationTimeFormatFixture photographerId(Long photographerId) {
        this.photographerId = photographerId;
        return this;
    }

    public ReservationTimeFormatFixture formatName(String formatName) {
        this.formatName = formatName;
        return this;
    }

    public ReservationTimeFormatFixture startTimeList(List<LocalTime> startTimeList) {
        this.startTimeList = startTimeList;
        return this;
    }

    public ReservationTimeFormat build() {
        return ReservationTimeFormat.builder()
            .id(this.id)
            .photographerId(this.photographerId)
            .formatName(this.formatName)
            .startTimeList(this.startTimeList)
            .build();
    }
}
