package com.cuk.catsnap.domain.review.repository;

import com.cuk.catsnap.domain.review.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

}
