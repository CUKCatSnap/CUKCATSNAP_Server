package net.catsnap.domain.reservation.dto;

import java.util.List;

public record HolidayListResponse(
    List<HolidayResponse> holidayList
) {

    public static HolidayListResponse from(List<HolidayResponse> holidayList) {
        return new HolidayListResponse(holidayList);
    }
}
