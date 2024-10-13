package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.review.entity.Review;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "place_subscribe_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceSubscribeNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;
}
