package net.catsnap.domain.review.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.entity.ReviewLike;
import net.catsnap.domain.review.repository.ReviewLikeRepository;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional(readOnly = true)
    Long getReviewLikeCount(Long reviewId) {
        return reviewLikeRepository.countByReviewIdAndLiked(reviewId, true);
    }

    @Transactional(readOnly = true)
    Boolean isMeReviewLiked(Long reviewId) {
        CatsnapAuthority authority = GetAuthenticationInfo.getAuthority();
        if (authority == CatsnapAuthority.MEMBER) {
            Long id = GetAuthenticationInfo.getUserId();
            return reviewLikeRepository.findByReviewIdAndMemberId(reviewId, id)
                .map(ReviewLike::getLiked)
                .orElse(false);
        } else if (authority == CatsnapAuthority.PHOTOGRAPHER) {
            Long id = GetAuthenticationInfo.getUserId();
            return reviewLikeRepository.findByReviewIdAndPhotographerId(reviewId, id)
                .map(ReviewLike::getLiked)
                .orElse(false);
        } else {
            return false;
        }
    }
}
