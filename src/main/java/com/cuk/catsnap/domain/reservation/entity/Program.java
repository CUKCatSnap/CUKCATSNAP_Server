package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
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


@Entity(name = "program")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Program extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    private String title;
    private String content;
    private Long price;

    @Column(name = "duration_minutes")
    private Long durationMinutes;
    private Long version;
}
