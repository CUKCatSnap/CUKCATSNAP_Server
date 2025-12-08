package net.catsnap.domain.reservation.entity;

/*
 * 작가가 예약 받을 시간 형식을 만들 때 사용되는 요일을 나타내는 Enum
 */

import lombok.Getter;

@Getter
public enum Weekday {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7),
    HOLIDAY(8);

    private final int weight;

    Weekday(int weight) {
        this.weight = weight;
    }
}
