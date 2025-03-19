package net.catsnap.domain.review.repository;

import net.catsnap.domain.review.entity.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Slice<Review> findAllByMemberId(Long memberId, Pageable pageable);
}
