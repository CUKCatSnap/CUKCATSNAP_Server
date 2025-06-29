package net.catsnap.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginUser;
import net.catsnap.domain.feed.dto.request.FeedCommentPostRequest;
import net.catsnap.domain.feed.dto.response.CommentListResponse;
import net.catsnap.domain.feed.dto.response.FeedCommentResponse;
import net.catsnap.domain.feed.service.FeedCommentLikeService;
import net.catsnap.domain.feed.service.FeedCommentService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드 댓글 관련 API", description = "피드의 댓글과 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedCommentController {

    private final FeedCommentService feedCommentService;
    private final FeedCommentLikeService feedCommentLikeService;

    @Operation(summary = "특정 피드의 댓글을 조회하는 API(구현 완료)", description = "특정 피드의 댓글을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @AnyUser
    @GetMapping("/{feedId}/comment")
    public ResponseEntity<ResultResponse<SlicedData<CommentListResponse>>> getFeedComment(
        @PathVariable("feedId")
        Long feedId,
        @UserId
        Long userId,
        @PageableDefault(page = 0, size = 10)
        Pageable pageable
    ) {
        SlicedData<CommentListResponse> commentListResponse = feedCommentService.getFeedComments(
            feedId, userId, pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, commentListResponse);
    }

    @Operation(summary = "피드에 댓글을 작성하는 API(구현 완료)", description = "피드에 댓글을 작성하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SC001", description = "성공적으로 데이터를 생성했습니다.")
    })
    @LoginUser
    @PostMapping("/{feedId}/comment")
    public ResponseEntity<ResultResponse<FeedCommentResponse>> postFeedComment(
        @PathVariable("feedId")
        Long feedId,
        @UserId
        Long userId,
        @Schema(description = "대댓글을 작성할 경우 부모 댓글의 id, 아니라면 null")
        @RequestParam(value = "parentCommentId", required = false)
        Long parentCommentId,
        @RequestBody
        FeedCommentPostRequest feedCommentPostRequest
    ) {
        FeedCommentResponse feedCommentResponse = feedCommentService.postFeedComment(
            feedId, userId, parentCommentId, feedCommentPostRequest);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE, feedCommentResponse);
    }

    @Operation(summary = "피드에 댓글을 삭제하는 API", description = "피드에 댓글을 삭제하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SF002", description = "성공적으로 피드의 댓글을 삭제하였습니다.")
    })
    @DeleteMapping("/comment/{commentId}")
    public ResultResponse<?> deleteFeedComment(
        @Parameter(description = "feed comment id")
        @PathVariable("commentId")
        Long feedCommentId
    ) {
        return null;
    }

    @Operation(summary = "피드 댓글의 좋아요를 토글하는 API(구현 완료)", description = "피드 댓글의 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC001", description = "성공적으로 데이터를 생성했습니다.")
    })
    @LoginUser
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<ResultResponse<Void>> feedCommentLikeToggle(
        @UserId
        Long userId,
        @PathVariable("commentId")
        Long feedCommentId
    ) {
        feedCommentLikeService.toggleFeedCommentLike(feedCommentId, userId);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE);
    }
}
