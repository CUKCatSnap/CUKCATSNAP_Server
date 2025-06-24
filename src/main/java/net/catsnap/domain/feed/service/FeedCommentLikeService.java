package net.catsnap.domain.feed.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.repository.FeedCommentLikeRepository;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeCountDto;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeMeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentLikeService {

    private final FeedCommentLikeRepository feedCommentLikeRepository;

    @Transactional(readOnly = true)
    public List<FeedCommentLikeCountDto> getFeedCommentLikeCount(List<Long> feedCommentIdList) {
        return feedCommentLikeRepository.countLikesByFeedCommentIdList(feedCommentIdList);
    }

    @Transactional(readOnly = true)
    public List<FeedCommentLikeMeDto> getFeedCommentLikeMe(List<Long> feedCommentIdList,
        Long userId) {
        // 좋아요 누른 댓글만 조회
        List<FeedCommentLikeMeDto> likedList =
            feedCommentLikeRepository.findLikedCommentsByUser(feedCommentIdList, userId);

        // Map으로 변환 (feedCommentId → true)
        Set<Long> likedIds = likedList.stream()
            .map(FeedCommentLikeMeDto::getFeedCommentId)
            .collect(Collectors.toSet());

        // 전체 댓글 ID에 대해 true/false로 매핑
        return feedCommentIdList.stream()
            .map(id -> new FeedCommentLikeMeDto(id, likedIds.contains(id)))
            .toList();
    }
}
