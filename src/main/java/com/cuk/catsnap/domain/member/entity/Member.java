package com.cuk.catsnap.domain.member.entity;

import com.cuk.catsnap.domain.feed.entity.FeedComment;
import com.cuk.catsnap.domain.feed.entity.FeedLike;
import com.cuk.catsnap.domain.notification.entity.Notification;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.review.entity.Review;
import com.cuk.catsnap.domain.review.entity.ReviewLike;
import com.cuk.catsnap.domain.social.entity.PhotographerBlock;
import com.cuk.catsnap.domain.social.entity.PhotographerSubscribe;
import com.cuk.catsnap.domain.social.entity.PlaceSubscribe;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "sns_type")
    @Enumerated(EnumType.STRING)
    private SnsType snstype;

    @Column(name="sns_id")
    private String snsId;

    @Column(name="sns_connect_date")
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
