package net.catsnap.domain.review.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import net.catsnap.domain.reservation.entity.Reservation;
import net.catsnap.domain.reservation.repository.ReservationRepository;
import net.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import net.catsnap.domain.review.dto.request.PostReviewRequest;
import net.catsnap.domain.review.entity.Review;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.review.repository.ReviewLikeRepository;
import net.catsnap.domain.review.repository.ReviewPhotoRepository;
import net.catsnap.domain.review.repository.ReviewRepository;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.repository.MemberRepository;
import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.domain.user.photographer.repository.PhotographerRepository;
import net.catsnap.global.Exception.authority.OwnershipNotFoundException;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import net.catsnap.global.aws.s3.ImageClient;
import net.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import net.catsnap.support.fixture.MemberFixture;
import net.catsnap.support.fixture.PhotographerFixture;
import net.catsnap.support.fixture.PostReviewRequestFixture;
import net.catsnap.support.fixture.PresignedUrlResponseFixture;
import net.catsnap.support.fixture.ReservationFixture;
import net.catsnap.support.fixture.ReviewFixture;
import net.catsnap.support.fixture.ReviewLikeFixture;
import net.catsnap.support.security.MemberSecurityContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ImageClient imageClient;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReviewPhotoRepository reviewPhotoRepository;
    @Mock
    ReviewLikeRepository reviewLikeRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PhotographerRepository photographerRepository;

    @Nested
    class 리뷰_작성_테스트 {

        @BeforeEach
        void beforeEach() {
            MemberSecurityContext.setContext();
        }

        @AfterEach
        void afterEach() {
            MemberSecurityContext.clearContext();
        }

        @Test
        void 리뷰_작성_완료() {
            // given
            Member member = MemberFixture.member()
                .id(MemberSecurityContext.MEMBER_ID)
                .build();
            Photographer photographer = PhotographerFixture.photographer()
                .build();
            Reservation reservation = ReservationFixture.reservation()
                .id(1L)
                .member(member)
                .photographer(photographer)
                .build();
            PostReviewRequest postReviewRequest = PostReviewRequestFixture.postReviewRequest()
                .build();
            PresignedUrlResponse presignedUrlResponse = PresignedUrlResponseFixture.presignedUrlResponse()
                .build();

            given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));
            given(reviewRepository.save(any()))
                .willReturn(null);
            given(imageClient.getUploadImageUrl(any()))
                .willReturn(presignedUrlResponse);

            //when
            ReviewPhotoPresignedURLResponse dto = reviewService.postReview(postReviewRequest);

            //then
            verify(reviewRepository, times(1)).save(any());
            verify(reviewPhotoRepository, times(1)).saveAll(any());
            Assertions.assertThat(dto.presignedURL().get(0))
                .isEqualTo(presignedUrlResponse.presignedURL());
            Assertions.assertThat(dto.photoURL().get(0))
                .isEqualTo(imageClient.getDownloadImageUrl(presignedUrlResponse.uuidFileName()));
        }

        @Test
        void 리뷰를_작성하고자_하는_예약이_존재하지_않음() {
            // given
            Member member = MemberFixture.member()
                .id(MemberSecurityContext.MEMBER_ID)
                .build();
            Photographer photographer = PhotographerFixture.photographer()
                .build();
            Reservation reservation = ReservationFixture.reservation()
                .id(1L)
                .member(member)
                .photographer(photographer)
                .build();
            PostReviewRequest postReviewRequest = PostReviewRequestFixture.postReviewRequest()
                .build();

            given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.empty());

            // when, then
            Assertions.assertThatThrownBy(() -> reviewService.postReview(postReviewRequest))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void 리뷰를_작성하고자_하는_사람이_해당_예약의_소유자가_아님() {
            // given
            Member anotherMember = MemberFixture.member()
                .id(2L)
                .build();
            Photographer photographer = PhotographerFixture.photographer()
                .build();
            Reservation reservation = ReservationFixture.reservation()
                .id(1L)
                .member(anotherMember)
                .photographer(photographer)
                .build();
            PostReviewRequest postReviewRequest = PostReviewRequestFixture.postReviewRequest()
                .build();

            given(reservationRepository.findById(reservation.getId()))
                .willReturn(Optional.of(reservation));

            // when, then
            Assertions.assertThatThrownBy(() -> reviewService.postReview(postReviewRequest))
                .isInstanceOf(OwnershipNotFoundException.class);
        }
    }

    @Nested
    class 리뷰_좋아요_토글 {

        @Nested
        class 사용자_좋아요_토글 {

            @BeforeEach
            void beforeEach() {
                MemberSecurityContext.setContext();
            }

            @AfterEach
            void afterEach() {
                MemberSecurityContext.clearContext();
            }

            @Test
            void 사용자_좋아요_최초_클릭() {
                // given
                Member member = MemberFixture.member()
                    .id(MemberSecurityContext.MEMBER_ID)
                    .build();
                Review review = ReviewFixture.review()
                    .id(1L)
                    .build();

                // 좋아요를 처음 누르는 경우
                given(
                    reviewLikeRepository.findByReviewIdAndUserId(review.getId(), member.getId()))
                    .willReturn(Optional.empty());
                given(memberRepository.getReferenceById(member.getId()))
                    .willReturn(member);
                given(reviewRepository.findById(1L))
                    .willReturn(Optional.of(review));

                //when
                reviewService.toggleReviewLike(review.getId(), member.getId());

                //then
                verify(reviewLikeRepository, times(1)).save(any());
            }

            @Test
            void 사용자_좋아요_2번째_클릭_좋아요_취소() {
                // given
                Member member = MemberFixture.member()
                    .id(MemberSecurityContext.MEMBER_ID)
                    .build();
                Review review = ReviewFixture.review()
                    .id(1L)
                    .build();
                ReviewLike reviewLike = ReviewLikeFixture.reviewLike()
                    .id(1L)
                    .build();

                // 좋아요를 누른 적 있는 경우
                given(
                    reviewLikeRepository.findByReviewIdAndUserId(review.getId(), member.getId()))
                    .willReturn(Optional.of(reviewLike));

                //when
                reviewService.toggleReviewLike(review.getId(), member.getId());

                //then
                verify(reviewLikeRepository, times(0)).save(reviewLike);
            }

        }
    }
}