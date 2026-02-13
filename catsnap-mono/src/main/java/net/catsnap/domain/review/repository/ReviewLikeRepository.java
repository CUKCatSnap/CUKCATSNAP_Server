package net.catsnap.domain.review.repository;

import java.util.Optional;
import net.catsnap.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Long countByReviewId(Long reviewId);

    Optional<ReviewLike> findByReviewIdAndUserId(Long reviewId, Long userId);
}
