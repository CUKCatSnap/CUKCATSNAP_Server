package com.cuk.catsnap.domain.review.entity;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.notification.entity.PlaceSubscribeNotification;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="review")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="photographer_id")
    private Photographer photographer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;

    @Column(name="place_score")
    private Integer placeScore;

    @Column(name="photographer_score")
    private Integer photographerScore;

    private String content;


    // OneToMany

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikeList;

    @OneToMany(mappedBy = "review")
    private List<ReviewPhoto> reviewPhotoList;

    @OneToMany(mappedBy = "review")
    private List<PlaceSubscribeNotification> placeSubscribeNotificationList;
}
