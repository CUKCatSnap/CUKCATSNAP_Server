package com.cuk.catsnap.domain.feed.controller;

import com.cuk.catsnap.domain.feed.dto.FeedRequest;
import com.cuk.catsnap.domain.feed.dto.FeedResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "피드 관련 API", description = "피드와 관련된 API입니다.")
@RestController
@RequestMapping("/feed")
public class FeedController {

    @Operation(summary = "해당 피드의 모든 댓글을 조회하는 API", description = "해당 피드의 모든 댓글을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SF000", description = "해당 피드의 모든 댓글을 조회했습니다.")
    })
    @GetMapping("/comment/{feedId}")
    public ResultResponse<PagedData<FeedResponse.FeedCommentList>> getFeedComment(
            @Parameter(description = "feed id")
            @PathVariable("feedId")
            Long feedId) {
        return null;
    }

    @Operation(summary = "피드에 댓글을 작성하는 API", description = "피드에 댓글을 작성하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SF001", description = "성공적으로 피드에 댓글을 작성하였습니다.")
    })
    @PostMapping("/comment")
    public ResultResponse<?> postFeedComment(
            @RequestBody
            FeedRequest.PostFeedComment postFeedComment
    ) {
        return null;
    }

    @Operation(summary = "피드에 댓글을 삭제하는 API", description = "피드에 댓글을 삭제하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SF002", description = "성공적으로 피드의 댓글을 삭제하였습니다.")
    })
    @DeleteMapping("/comment/{feedCommentId}")
    public ResultResponse<?> deleteFeedComment(
            @Parameter(description = "feed comment id")
            @PathVariable("feedCommentId")
            Long feedCommentId
    ) {
        return null;
    }

    @Operation(summary = "피드 댓글의 좋아요를 토글하는 API", description = "피드 댓글의 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @PostMapping("/comment/like/{feedCommentId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SF003", description = "성공적으로 피드 댓글의 좋아요를 토글하였습니다.")
    })
    public ResultResponse<?> feedCommentLikeToggle(
            @PathVariable("feedCommentId")
            Long feedCommentId
    ) {
        return null;
    }

    @Operation(summary = "피드에 좋아요를 토글하는 API", description = "피드의 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @PostMapping("/like/{feedId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SF004", description = "성공적으로 피드의 좋아요를 토글하였습니다.")
    })
    public ResultResponse<?> feedLikeToggle(
            @PathVariable("feedId")
            Long feedId
    ) {
        return null;
    }
}
