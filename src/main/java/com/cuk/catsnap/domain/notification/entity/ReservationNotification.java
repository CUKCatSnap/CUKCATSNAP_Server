package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.entity.ReservationState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "reservation_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @Enumerated(EnumType.STRING)
    @Column(name="rservation_notification_type")
    private ReservationState reservationNotificationType;
}
