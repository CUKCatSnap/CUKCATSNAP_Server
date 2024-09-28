package com.cuk.catsnap.domain.reservation.entity;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
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
public class Review {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id")
    private Review review;

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
}
