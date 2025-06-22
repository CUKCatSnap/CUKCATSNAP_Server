package net.catsnap.domain.feed.repository;

import java.util.Optional;
import net.catsnap.domain.feed.entity.FeedLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    Long countByFeedId(Long feedId);

    Optional<FeedLike> findByFeedIdAndUserId(Long feedId, Long userId);
}
