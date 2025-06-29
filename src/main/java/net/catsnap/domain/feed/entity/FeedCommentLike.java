package net.catsnap.domain.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.domain.user.entity.User;

@Entity
@Table(name = "feed_comment_like")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User likeUser;

    @ManyToOne
    @JoinColumn(name = "feed_comment_id")
    private FeedComment feedComment;

    public FeedCommentLike(User likeUser, FeedComment feedComment) {
        this.likeUser = likeUser;
        this.feedComment = feedComment;
    }
}
