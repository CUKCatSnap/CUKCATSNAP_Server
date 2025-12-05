package net.catsnap.domain.feed.repository;

import net.catsnap.domain.feed.entity.FeedComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    long countByFeedId(Long feedId);

    Slice<FeedComment> findByFeedIdAndParentCommentIsNull(Long feedId, Pageable pageable);
}
