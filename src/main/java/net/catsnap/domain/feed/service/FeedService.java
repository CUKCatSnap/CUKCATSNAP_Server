package net.catsnap.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.dto.response.FeedDetailResponse;
import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.repository.FeedRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedLikeService feedLikeService;
    private final FeedCommentService feedCommentService;

    @Transactional(readOnly = true)
    public FeedDetailResponse getFeedDetail(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 피드를 찾을 수 없습니다."));
        long likeCount = feedLikeService.getFeedLikeCount(feedId);
        boolean isLiked = feedLikeService.getFeedLikes(feedId, userId);
        long commentCount = feedCommentService.getFeedCommentCount(feedId);
        return FeedDetailResponse.of(feed, likeCount, isLiked, commentCount);
    }
}
