package com.cuk.catsnap.domain.photographer.entity;

import com.cuk.catsnap.domain.feed.entity.Feed;
import com.cuk.catsnap.domain.feed.entity.FeedComment;
import com.cuk.catsnap.domain.feed.entity.FeedLike;
import com.cuk.catsnap.domain.notification.entity.Notification;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.review.entity.Review;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="photograph")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photographer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="photographer_id")
    private Long id;

    private String identifier;
    private String password;
    private String nickname;
    private LocalDate birthday;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_photo")
    private String profilePhoto;


    // OneToMany

    @OneToMany(mappedBy = "photographer")
    private List<Reservation> ReservationList;

    @OneToMany(mappedBy = "photographer")
    private List<Review> ReviewList;

    @OneToMany(mappedBy = "photographer")
    private List<Feed> feedList;

    @OneToMany(mappedBy = "photographer")
    private List<FeedLike> feedLikeList;

    @OneToMany(mappedBy = "photographer")
    private List<FeedComment> feedCommentList;

    @OneToMany(mappedBy = "photographer")
    private List<Notification> notificationList;
}
