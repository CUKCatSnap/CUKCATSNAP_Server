package com.cuk.catsnap.domain.notification.entity;

import com.cuk.catsnap.domain.feed.entity.FeedLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "feed_like_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedLikeNotification extends Notification {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_like_id")
    private FeedLike feedLike;
}
