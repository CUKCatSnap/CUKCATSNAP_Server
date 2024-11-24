package net.catsnap.domain.notification.entity;

import net.catsnap.domain.feed.entity.FeedComment;
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
@Table(name = "feed_comment_notification")
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FeedCommentNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_comment_id")
    private FeedComment feedComment;
}
