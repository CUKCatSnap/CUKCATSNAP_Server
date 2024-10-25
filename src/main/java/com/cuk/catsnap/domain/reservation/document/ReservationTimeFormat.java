package com.cuk.catsnap.domain.reservation.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;


/*
* ReservationTimeFormat은 내부적으로 리스트를 가지고 있기 때문에 Nosql로 저장하는 것이 적합.
*
 */
//@Entity(name = "reservation_time_format")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTimeFormat {

    private String id;
    private Long photographerId;
    private String formatName;

    private List<LocalTime> startTimeList;
}
