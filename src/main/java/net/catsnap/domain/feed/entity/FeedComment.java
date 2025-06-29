package net.catsnap.domain.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.domain.user.entity.User;
import net.catsnap.global.entity.BaseTimeEntity;

@Entity
@Table(name = "feed_comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private FeedComment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    public FeedComment(Feed feed, FeedComment parentComment, User user, String content) {
        this.feed = feed;
        this.parentComment = parentComment;
        this.user = user;
        this.content = content;
    }

    //OneToMany
    @OneToMany(mappedBy = "parentComment")
    private List<FeedComment> children;
}
