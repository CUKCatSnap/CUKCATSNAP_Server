package com.cuk.catsnap.domain.review.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cuk.catsnap.domain.member.entity.Member;
import com.cuk.catsnap.domain.photographer.entity.Photographer;
import com.cuk.catsnap.domain.reservation.entity.Reservation;
import com.cuk.catsnap.domain.reservation.repository.ReservationRepository;
import com.cuk.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import com.cuk.catsnap.domain.review.dto.request.PostReviewRequest;
import com.cuk.catsnap.domain.review.repository.ReviewPhotoRepository;
import com.cuk.catsnap.domain.review.repository.ReviewRepository;
import com.cuk.catsnap.global.Exception.authority.OwnershipNotFoundException;
import com.cuk.catsnap.global.aws.s3.ImageClient;
import com.cuk.catsnap.global.aws.s3.dto.PresignedUrlResponse;
import com.cuk.catsnap.support.fixture.MemberFixture;
import com.cuk.catsnap.support.fixture.PhotographerFixture;
import com.cuk.catsnap.support.fixture.PostReviewRequestFixture;
import com.cuk.catsnap.support.fixture.PresignedUrlResponseFixture;
import com.cuk.catsnap.support.fixture.ReservationFixture;
import com.cuk.catsnap.support.security.MemberSecurityContext;
import java.util.Optional;
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

    @BeforeEach
    void beforeEach() {
        MemberSecurityContext.setContext();
    }

    @AfterEach
    void afterEach() {
        MemberSecurityContext.clearContext();
    }

    @Nested
    class 리뷰_작성_테스트 {

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
                .isInstanceOf(OwnershipNotFoundException.class);
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

            // when, then
            Assertions.assertThatThrownBy(() -> reviewService.postReview(postReviewRequest))
                .isInstanceOf(OwnershipNotFoundException.class);
        }
    }
}