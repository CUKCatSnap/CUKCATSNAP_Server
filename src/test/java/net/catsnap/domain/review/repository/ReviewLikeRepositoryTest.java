package net.catsnap.domain.review.repository;

import java.util.Optional;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.support.fixture.MemberFixture;
import net.catsnap.support.fixture.PhotographerFixture;
import net.catsnap.support.fixture.ProgramFixture;
import net.catsnap.support.fixture.ReservationFixture;
import net.catsnap.support.fixture.ReviewFixture;
import net.catsnap.support.fixture.ReviewLikeFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReviewLikeRepositoryTest {

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    void 특정_리뷰에_좋아요_수를_조회() {
        //given
        Member member = MemberFixture.member()
            .build();
        Photographer photographer = PhotographerFixture.photographer()
            .build();
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();
        Reservation reservation = ReservationFixture.reservation()
            .member(member)
            .photographer(photographer)
            .program(program)
            .build();
        Review review = ReviewFixture.review()
            .member(member)
            .photographer(photographer)
            .reservation(reservation)
            .build();

        memberRepository.save(member);
        photographerRepository.save(photographer);
        programRepository.save(program);
        reservationRepository.save(reservation);
        reviewRepository.save(review);

        Member likedMember = MemberFixture.member()
            .build();
        Member unLikedMember = MemberFixture.member()
            .build();
        memberRepository.save(likedMember);
        memberRepository.save(unLikedMember);
        ReviewLike reviewLike = ReviewLikeFixture.reviewLike()
            .member(likedMember)
            .photographer(null)
            .review(review)
            .liked(true)
            .build();
        ReviewLike reviewUnliked = ReviewLikeFixture.reviewLike()
            .member(unLikedMember)
            .photographer(null)
            .review(review)
            .liked(false)
            .build();
        reviewLikeRepository.save(reviewLike);
        reviewLikeRepository.save(reviewUnliked);

        Photographer likedPhotographer = PhotographerFixture.photographer()
            .build();
        Photographer unlikedPhotographer = PhotographerFixture.photographer()
            .build();
        photographerRepository.save(likedPhotographer);
        photographerRepository.save(unlikedPhotographer);
        ReviewLike reviewLike2 = ReviewLikeFixture.reviewLike()
            .member(null)
            .photographer(likedPhotographer)
            .review(review)
            .liked(true)
            .build();
        ReviewLike reviewUnliked2 = ReviewLikeFixture.reviewLike()
            .member(null)
            .photographer(unlikedPhotographer)
            .review(review)
            .liked(false)
            .build();
        reviewLikeRepository.save(reviewLike2);
        reviewLikeRepository.save(reviewUnliked2);

        //when
        Long likeCount = reviewLikeRepository.countByReviewIdAndLiked(review.getId(), true);

        //then
        Assertions.assertThat(likeCount).isEqualTo(2);
    }


    @Test
    void 특정_맴버가_리뷰에_좋아요를_눌렀는지_확인() {
        //given
        Member member = MemberFixture.member()
            .build();
        Photographer photographer = PhotographerFixture.photographer()
            .build();
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();
        Reservation reservation = ReservationFixture.reservation()
            .member(member)
            .photographer(photographer)
            .program(program)
            .build();
        Review review = ReviewFixture.review()
            .member(member)
            .photographer(photographer)
            .reservation(reservation)
            .build();

        memberRepository.save(member);
        photographerRepository.save(photographer);
        programRepository.save(program);
        reservationRepository.save(reservation);
        reviewRepository.save(review);

        Member likedMember = MemberFixture.member()
            .build();
        Member unLikedMember = MemberFixture.member()
            .build();
        memberRepository.save(likedMember);
        memberRepository.save(unLikedMember);
        ReviewLike reviewLike = ReviewLikeFixture.reviewLike()
            .member(likedMember)
            .photographer(null)
            .review(review)
            .build();
        reviewLikeRepository.save(reviewLike);

        //when
        Optional<ReviewLike> liked = reviewLikeRepository.findByReviewIdAndMemberId(review.getId(),
            likedMember.getId());
        Optional<ReviewLike> unLiked = reviewLikeRepository.findByReviewIdAndMemberId(
            review.getId(),
            unLikedMember.getId());

        //then
        Assertions.assertThat(liked).isPresent();
        Assertions.assertThat(unLiked).isEmpty();
    }

    @Test
    void 특정_작가가_리뷰에_좋아요를_눌렀는지_확인() {
        //given
        Member member = MemberFixture.member()
            .build();
        Photographer photographer = PhotographerFixture.photographer()
            .build();
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();
        Reservation reservation = ReservationFixture.reservation()
            .member(member)
            .photographer(photographer)
            .program(program)
            .build();
        Review review = ReviewFixture.review()
            .member(member)
            .photographer(photographer)
            .reservation(reservation)
            .build();

        memberRepository.save(member);
        photographerRepository.save(photographer);
        programRepository.save(program);
        reservationRepository.save(reservation);
        reviewRepository.save(review);

        Photographer likedPhotographer = PhotographerFixture.photographer()
            .build();
        Photographer unlikedPhotographer = PhotographerFixture.photographer()
            .build();
        photographerRepository.save(likedPhotographer);
        photographerRepository.save(unlikedPhotographer);
        ReviewLike reviewLike = ReviewLikeFixture.reviewLike()
            .member(null)
            .photographer(likedPhotographer)
            .review(review)
            .build();
        reviewLikeRepository.save(reviewLike);

        //when
        Optional<ReviewLike> liked = reviewLikeRepository.findByReviewIdAndPhotographerId(
            review.getId(),
            likedPhotographer.getId());
        Optional<ReviewLike> unLiked = reviewLikeRepository.findByReviewIdAndPhotographerId(
            review.getId(),
            unlikedPhotographer.getId());

        //then
        Assertions.assertThat(liked).isPresent();
        Assertions.assertThat(unLiked).isEmpty();
    }
}