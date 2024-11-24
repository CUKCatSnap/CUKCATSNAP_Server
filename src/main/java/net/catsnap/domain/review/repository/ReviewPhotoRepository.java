package net.catsnap.domain.review.repository;

import net.catsnap.domain.review.entity.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, Long> {

}
