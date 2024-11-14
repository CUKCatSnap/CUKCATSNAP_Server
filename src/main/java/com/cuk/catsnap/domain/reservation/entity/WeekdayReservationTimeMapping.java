package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeekdayReservationTimeMapping extends BaseTimeEntity {

    @Id
    @Column(name = "weekday_reservation_time_mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Photographer photographer;


    /*
     * reservationTimeFormat은 Nosql의 도큐먼트.
     */
    @Column(name = "reservation_time_format_id")
    private String reservationTimeFormatId;

    @Column(name = "weekday")
    @Enumerated(EnumType.STRING)
    private Weekday weekday;

    public void updateReservationTimeFormatId(String reservationTimeFormatId) {
        this.reservationTimeFormatId = reservationTimeFormatId;
    }

    public void reservationTimeFormatIdToNull() {
        this.reservationTimeFormatId = null;
    }
}
