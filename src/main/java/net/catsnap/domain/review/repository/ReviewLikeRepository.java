package net.catsnap.domain.review.repository;

import net.catsnap.domain.review.entity.ReviewLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByIdAndMemberId(Long reviewLikeId, Long memberId);

    Optional<ReviewLike> findByIdAndPhotographerId(Long reviewLikeId, Long photographerId);
}