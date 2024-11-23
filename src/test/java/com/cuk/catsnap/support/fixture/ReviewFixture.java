package com.cuk.catsnap.support.fixture;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.review.entity.Review;

public class ReviewFixture {

    private Long id;
    private Member member = MemberFixture.member().build();
    private Photographer photographer = PhotographerFixture.photographer().build();
    private Reservation reservation = ReservationFixture.reservation().build();
    private Integer placeScore = 3;
    private Integer photographerScore = 3;
    private String content = "content";

    public static ReviewFixture review() {
        return new ReviewFixture();
    }

    public ReviewFixture id(Long id) {
        this.id = id;
        return this;
    }

    public ReviewFixture member(Member member) {
        this.member = member;
        return this;
    }

    public ReviewFixture photographer(Photographer photographer) {
        this.photographer = photographer;
        return this;
    }

    public Review build() {
        return Review.builder()
            .id(id)
            .member(member)
            .photographer(photographer)
            .reservation(reservation)
            .placeScore(placeScore)
            .photographerScore(photographerScore)
            .content(content)
            .build();
    }
}
