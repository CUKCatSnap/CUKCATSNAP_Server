package net.catsnap.domain.feed.entity;

import net.catsnap.domain.notification.entity.PhotographerSubscribeNotification;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.global.entity.BaseTimeEntity;
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

@Entity
@Table(name = "feed")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    private String content;

    //OneToMany

    @OneToMany(mappedBy = "feed")
    private List<FeedPhoto> feedPhotoList;

    @OneToMany(mappedBy = "feed")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "feed")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "feed")
    private List<PhotographerSubscribeNotification> photographerSubscribeNotificationList;
}
