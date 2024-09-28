package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.feed.entity.Feed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "photographer_subscribe_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerSubscribeNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_id")
    private Feed feed;
}
