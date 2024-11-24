package net.catsnap.domain.member.entity;

import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.entity.FeedLike;
import net.catsnap.domain.notification.entity.Notification;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.social.entity.PhotographerBlock;
import net.catsnap.domain.social.entity.PhotographerSubscribe;
import net.catsnap.domain.social.entity.PlaceSubscribe;
import net.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String identifier;
    private String password;
    private String nickname;
    private LocalDate birthday;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_photo_url")
    private String profilePhotoUrl;

    @Column(name = "sns_type")
    @Enumerated(EnumType.STRING)
    private SnsType snstype;

    @Column(name = "sns_id")
    private String snsId;

    @Column(name = "sns_connect_date")
    private LocalDateTime snsConnectDate;

    // OneToMany

    @OneToMany(mappedBy = "member")
    private List<MemberAgree> memberAgrees;

    @OneToMany(mappedBy = "member")
    private List<PhotographerSubscribe> photographerSubscribeList;

    @OneToMany(mappedBy = "member")
    private List<PhotographerBlock> photographerBlockList;

    @OneToMany(mappedBy = "member")
    private List<PlaceSubscribe> placeSubscribeList;

    @OneToMany(mappedBy = "member")
    private List<Reservation> ReservationList;

    @OneToMany(mappedBy = "member")
    private List<Review> reviewList;

    @OneToMany(mappedBy = "member")
    private List<ReviewLike> ReviewLikeList;

    @OneToMany(mappedBy = "member")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "member")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "member")
    private List<Notification> notificationList;
}
