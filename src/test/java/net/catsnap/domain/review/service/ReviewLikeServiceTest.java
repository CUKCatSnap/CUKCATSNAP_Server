package net.catsnap.domain.review.service;

import static org.mockito.BDDMockito.given;

import java.util.Optional;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.review.repository.ReviewLikeRepository;
import net.catsnap.domain.user.fakeuser.entity.FakeUser;
import net.catsnap.support.fixture.ReviewLikeFixture;
import net.catsnap.support.security.AnonymousSecurityContext;
import net.catsnap.support.security.MemberSecurityContext;
import net.catsnap.support.security.PhotographerSecurityContext;
import org.assertj.core.api.Assertions;
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
class ReviewLikeServiceTest {

    @InjectMocks
    private ReviewLikeService reviewLikeService;

    @Mock
    private ReviewLikeRepository reviewLikeRepository;

    @Test
    void 특정_리뷰에_좋아요_개수를_조회한다() {
        // given
        Long reviewId = 1L;
        given(reviewLikeRepository.countByReviewId(reviewId)).willReturn(123L);

        // when
        Long likedCount = reviewLikeService.getReviewLikeCount(reviewId);

        // then
        Assertions.assertThat(likedCount).isEqualTo(123L);
    }

    @Nested
    class 내가_해당_리뷰를_좋아요_했는지_조회한다 {

        @Test
        void 사용자가_해당_리뷰를_좋아요_했을_경우_true를_반환한다() {
            // given
            MemberSecurityContext.setContext();
            Long reviewId = 1L;
            ReviewLike reviewLike = ReviewLikeFixture.reviewLike().build();
            given(reviewLikeRepository.findByReviewIdAndUserId(reviewId,
                MemberSecurityContext.MEMBER_ID))
                .willReturn(Optional.of(reviewLike));
            //when
            Boolean isLiked = reviewLikeService.isMeReviewLiked(reviewId,
                MemberSecurityContext.MEMBER_ID);

            //then
            Assertions.assertThat(isLiked).isTrue();

            //cleanup
            MemberSecurityContext.clearContext();
        }

        @Test
        void 사용자가__해당_리뷰를_좋아요_하지_않았을_경우_false를_반환한다() {
            // given
            MemberSecurityContext.setContext();
            Long reviewId = 1L;
            given(reviewLikeRepository.findByReviewIdAndUserId(reviewId,
                MemberSecurityContext.MEMBER_ID))
                .willReturn(Optional.empty());
            //when
            Boolean isLiked = reviewLikeService.isMeReviewLiked(reviewId,
                MemberSecurityContext.MEMBER_ID);

            //then
            Assertions.assertThat(isLiked).isFalse();

            //cleanup
            MemberSecurityContext.clearContext();
        }

        @Test
        void 작가가_해당_리뷰를_좋아요_했을_경우_true를_반환한다() {
            // given
            PhotographerSecurityContext.setContext();
            Long reviewId = 1L;
            ReviewLike reviewLike = ReviewLikeFixture.reviewLike().build();
            given(reviewLikeRepository.findByReviewIdAndUserId(reviewId,
                PhotographerSecurityContext.Photographer_ID))
                .willReturn(Optional.of(reviewLike));
            //when
            Boolean isLiked = reviewLikeService.isMeReviewLiked(reviewId,
                PhotographerSecurityContext.Photographer_ID);

            //then
            Assertions.assertThat(isLiked).isTrue();

            //cleanup
            PhotographerSecurityContext.clearContext();
        }

        @Test
        void 작가__해당_리뷰를_좋아요_하지_않았을_경우_false를_반환한다() {
// given
            PhotographerSecurityContext.setContext();
            Long reviewId = 1L;
            given(reviewLikeRepository.findByReviewIdAndUserId(reviewId,
                PhotographerSecurityContext.Photographer_ID))
                .willReturn(Optional.empty());
            //when
            Boolean isLiked = reviewLikeService.isMeReviewLiked(reviewId,
                PhotographerSecurityContext.Photographer_ID);

            //then
            Assertions.assertThat(isLiked).isFalse();

            //cleanup
            PhotographerSecurityContext.clearContext();
        }

        @Test
        void 로그인_하지_않은_사용자는_false를_반환한다() {
            // given
            AnonymousSecurityContext.setContext();
            // when
            Boolean isLiked = reviewLikeService.isMeReviewLiked(1L, FakeUser.fakeUserId);

            // then
            Assertions.assertThat(isLiked).isFalse();
            AnonymousSecurityContext.clearContext();
        }
    }
}