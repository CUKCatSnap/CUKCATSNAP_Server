package net.catsnap.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.repository.FeedLikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;

    @Transactional(readOnly = true)
    public long getFeedLikeCount(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    @Transactional(readOnly = true)
    public boolean getFeedLikes(Long feedId, Long userId) {
        return feedLikeRepository.findByFeedIdAndUserId(feedId, userId).isPresent();

    }
}
