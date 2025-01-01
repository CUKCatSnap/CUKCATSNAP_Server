package net.catsnap.domain.reservation.dto;

import java.util.List;

public record MonthReservationCheckListResponse(
    List<MonthReservationCheckResponse> monthReservationCheckList
) {

    public static MonthReservationCheckListResponse from(
        List<MonthReservationCheckResponse> monthReservationCheckList) {
        return new MonthReservationCheckListResponse(monthReservationCheckList);
    }
}
