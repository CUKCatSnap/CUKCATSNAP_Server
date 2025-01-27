package net.catsnap.domain.review.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.repository.ReviewLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional(readOnly = true)
    Long getReviewLikeCount(Long reviewId) {
        return reviewLikeRepository.countByReviewId(reviewId);
    }

    @Transactional(readOnly = true)
    Boolean isMeReviewLiked(Long reviewId, Long userId) {
        return reviewLikeRepository.findByReviewIdAndUserId(reviewId, userId).isPresent();
    }
}
