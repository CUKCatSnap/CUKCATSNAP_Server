package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class WeekdayReservationTimeMapping {

    @Id
    @Column(name = "weekday_reservation_time_mapping_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "reservation_time_format_id")
    private ReservationTimeFormat reservationTimeFormat;

    @Column(name = "weekday")
    @Enumerated(EnumType.STRING)
    private Weekday weekday;
}
