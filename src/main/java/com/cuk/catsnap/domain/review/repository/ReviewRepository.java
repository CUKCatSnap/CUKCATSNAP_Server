package com.cuk.catsnap.domain.review.repository;

import com.cuk.catsnap.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
