package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity(name = "reservation_time_format")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationTimeFormat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_time_format_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    private String name;

    @Column(name = "start_time")
    private LocalTime startTime;
}
