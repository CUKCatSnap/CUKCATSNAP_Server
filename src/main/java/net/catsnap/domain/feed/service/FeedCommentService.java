package net.catsnap.domain.feed.service;

import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.repository.FeedCommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;

    @Transactional(readOnly = true)
    public long getFeedCommentCount(Long feedId) {
        return feedCommentRepository.countByFeedId(feedId);
    }
}
