package net.catsnap.domain.review.repository;

import java.util.Optional;
import net.catsnap.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByIdAndMemberId(Long reviewLikeId, Long memberId);

    Optional<ReviewLike> findByIdAndPhotographerId(Long reviewLikeId, Long photographerId);

    Long countByReviewIdAndLiked(Long reviewId, Boolean liked);

    Optional<ReviewLike> findByReviewIdAndMemberId(Long reviewId, Long memberId);

    Optional<ReviewLike> findByReviewIdAndPhotographerId(Long reviewId, Long photographerId);
}
