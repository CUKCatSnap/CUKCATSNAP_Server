package net.catsnap.domain.feed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.dto.response.CommentListResponse;
import net.catsnap.domain.feed.dto.response.CommentResponse;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.repository.FeedCommentRepository;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeCountDto;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeMeDto;
import net.catsnap.global.result.SlicedData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentService {

    private final FeedCommentRepository feedCommentRepository;
    private final FeedCommentLikeService feedCommentLikeService;

    private static final Long DEFAULT_LIKE_COUNT = 0L;
    private static final boolean DEFAULT_IS_LIKED = false;

    @Transactional(readOnly = true)
    public long getFeedCommentCount(Long feedId) {
        return feedCommentRepository.countByFeedId(feedId);
    }

    @Transactional(readOnly = true)
    public SlicedData<CommentListResponse> getFeedComments(long feedId, long userId,
        Pageable pageable) {

        Slice<FeedComment> commentSlice = feedCommentRepository.findByFeedIdAndParentCommentIsNull(
            feedId, pageable);

        // 댓글 ID 리스트 추출
        List<Long> commentIds = commentSlice.getContent().stream()
            .map(FeedComment::getId)
            .toList();

        // 좋아요 수
        Map<Long, Long> likeCountMap = feedCommentLikeService
            .getFeedCommentLikeCount(commentIds).stream()
            .collect(Collectors.toMap(
                FeedCommentLikeCountDto::getFeedCommentId,
                FeedCommentLikeCountDto::getLikeCount
            ));

        // 내가 좋아요 눌렀는지 여부 (true/false 모두 포함됨)
        Map<Long, Boolean> isLikedMap = feedCommentLikeService
            .getFeedCommentLikeMe(commentIds, userId).stream()
            .collect(Collectors.toMap(
                FeedCommentLikeMeDto::getFeedCommentId,
                FeedCommentLikeMeDto::getIsLiked
            ));

        // 응답 생성
        List<CommentResponse> commentResponses = commentSlice.getContent().stream()
            .map(comment -> CommentResponse.of(
                comment,
                likeCountMap.getOrDefault(comment.getId(), DEFAULT_LIKE_COUNT),
                isLikedMap.getOrDefault(comment.getId(), DEFAULT_IS_LIKED)
            ))
            .toList();
        return SlicedData.of(
            CommentListResponse.from(commentResponses),
            commentSlice.isFirst(),
            commentSlice.isLast()
        );
    }
}
