package net.catsnap.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.entity.FeedLike;
import net.catsnap.domain.feed.repository.FeedLikeRepository;
import net.catsnap.domain.feed.repository.FeedRepository;
import net.catsnap.domain.user.entity.User;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedLikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public long getFeedLikeCount(Long feedId) {
        return feedLikeRepository.countByFeedId(feedId);
    }

    @Transactional(readOnly = true)
    public boolean getFeedLikes(Long feedId, Long userId) {
        return feedLikeRepository.findByFeedIdAndUserId(feedId, userId).isPresent();

    }

    @Transactional
    public void toggleFeedLike(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 피드가 존재하지 않습니다"));

        User user = userRepository.getReferenceById(userId);

        feedLikeRepository.findByFeedIdAndUserId(feedId, userId)
            .ifPresentOrElse(
                feedLikeRepository::delete,
                () -> {
                    feedLikeRepository.save(new FeedLike(feed, user));
                }
            );
    }
}
