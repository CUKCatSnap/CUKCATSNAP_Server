package net.catsnap.domain.feed.repository;

import net.catsnap.domain.feed.entity.FeedPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPhotoRepository extends JpaRepository<FeedPhoto, Long> {

}
