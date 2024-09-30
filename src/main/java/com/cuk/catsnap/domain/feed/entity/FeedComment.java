package com.cuk.catsnap.domain.feed.entity;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.notification.entity.FeedCommentNotification;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="feed_comment")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="feed_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="feed_id")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_comment_id")
    private FeedComment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photographer_id")
    private Photographer photographer;

    private String content;

    //OneToMany
    @OneToMany(mappedBy = "parentComment")
    private List<FeedComment> children;

    @OneToMany(mappedBy = "feedComment")
    private List<FeedCommentNotification> feedCommentNotifications;
}
