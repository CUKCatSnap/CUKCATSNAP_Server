package net.catsnap.domain.notification.entity;

import net.catsnap.domain.feed.entity.Feed;
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
@Table(name = "photographer_subscribe_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PhotographerSubscribeNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;
}
