package com.cuk.catsnap.domain.photographer.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerSetting {

    private String id;
    private Long photographerId;
    private Boolean autoReservationAccept;
    private Boolean enableOverBooking;
    private Long preReservationDays;
    private String announcement;
}
