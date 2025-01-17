package net.catsnap.support.fixture;

import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.photographer.entity.Photographer;

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

    public ReviewFixture reservation(Reservation reservation) {
        this.reservation = reservation;
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
