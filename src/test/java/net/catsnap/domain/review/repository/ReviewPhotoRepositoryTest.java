package net.catsnap.domain.review.repository;

import java.util.List;
import net.catsnap.domain.member.entity.Member;
import net.catsnap.domain.member.repository.MemberRepository;
import net.catsnap.domain.photographer.entity.Photographer;
import net.catsnap.domain.photographer.repository.PhotographerRepository;
import net.catsnap.domain.reservation.entity.Program;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.repository.ProgramRepository;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewPhoto;
import net.catsnap.support.fixture.MemberFixture;
import net.catsnap.support.fixture.PhotographerFixture;
import net.catsnap.support.fixture.ProgramFixture;
import net.catsnap.support.fixture.ReservationFixture;
import net.catsnap.support.fixture.ReviewFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReviewPhotoRepositoryTest {

    @Autowired
    private ReviewPhotoRepository reviewPhotoRepository;

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
    void 리뷰_아이디로_리뷰_사진_조회() {
        //given
        Member member = MemberFixture.member()
            .build();
        Photographer photographer = PhotographerFixture.photographer()
            .build();
        Program program = ProgramFixture.program()
            .photographer(photographer)
            .build();
        Reservation reservation1 = ReservationFixture.reservation()
            .member(member)
            .photographer(photographer)
            .program(program)
            .build();
        Reservation reservation2 = ReservationFixture.reservation()
            .member(member)
            .photographer(photographer)
            .program(program)
            .build();
        Review review1 = ReviewFixture.review()
            .member(member)
            .photographer(photographer)
            .reservation(reservation1)
            .build();
        Review review2 = ReviewFixture.review()
            .member(member)
            .photographer(photographer)
            .reservation(reservation2)
            .build();
        memberRepository.save(member);
        photographerRepository.save(photographer);
        programRepository.save(program);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        ReviewPhoto reviewPhoto1 = ReviewPhoto.builder()
            .review(review1)
            .build();
        ReviewPhoto reviewPhoto2 = ReviewPhoto.builder()
            .review(review1)
            .build();
        ReviewPhoto reviewPhoto3 = ReviewPhoto.builder()
            .review(review1)
            .build();
        ReviewPhoto reviewPhoto4 = ReviewPhoto.builder()
            .review(review2)
            .build();
        ReviewPhoto reviewPhoto5 = ReviewPhoto.builder()
            .review(review2)
            .build();
        reviewPhotoRepository.save(reviewPhoto1);
        reviewPhotoRepository.save(reviewPhoto2);
        reviewPhotoRepository.save(reviewPhoto3);
        reviewPhotoRepository.save(reviewPhoto4);
        reviewPhotoRepository.save(reviewPhoto5);

        //when
        List<ReviewPhoto> reviewPhotoList = reviewPhotoRepository.findAllByReviewId(
            review1.getId());

        //then
        Assertions.assertThat(reviewPhotoList.size()).isEqualTo(3);
    }

}