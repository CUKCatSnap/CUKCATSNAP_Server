package net.catsnap.domain.feed.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.feed.dto.request.FeedCommentPostRequest;
import net.catsnap.domain.feed.dto.response.CommentListResponse;
import net.catsnap.domain.feed.dto.response.CommentResponse;
import net.catsnap.domain.feed.dto.response.FeedCommentResponse;
import net.catsnap.domain.feed.entity.Feed;
import net.catsnap.domain.feed.entity.FeedComment;
import net.catsnap.domain.feed.repository.FeedCommentRepository;
import net.catsnap.domain.feed.repository.FeedRepository;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeCountDto;
import net.catsnap.domain.feed.repository.dto.FeedCommentLikeMeDto;
import net.catsnap.domain.user.entity.User;
import net.catsnap.domain.user.repository.UserRepository;
import net.catsnap.global.Exception.authority.ResourceNotFoundException;
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
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public FeedCommentResponse postFeedComment(
        Long feedId,
        Long userId,
        Long parentCommentId,
        FeedCommentPostRequest feedCommentPostRequest
    ) {
        Feed feed = feedRepository.findById(feedId)
            .orElseThrow(() -> new ResourceNotFoundException("해당 피드를 찾을 수 없습니다."));

        FeedComment parentComment = null;
        if (parentCommentId != null) {
            parentComment = feedCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 부모 댓글을 찾을 수 없습니다."));
        }

        User user = userRepository.getReferenceById(userId);

        FeedComment newComment = new FeedComment(feed, parentComment, user,
            feedCommentPostRequest.comment());
        FeedComment savedComment = feedCommentRepository.save(newComment);

        return FeedCommentResponse.of(savedComment.getId());
    }
}
