package net.catsnap.domain.feed.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginPhotographer;
import net.catsnap.domain.auth.interceptor.LoginUser;
import net.catsnap.domain.feed.dto.request.FeedPostRequest;
import net.catsnap.domain.feed.dto.response.FeedDetailResponse;
import net.catsnap.domain.feed.dto.response.FeedPhotoPresignedURLResponse;
import net.catsnap.domain.feed.service.FeedLikeService;
import net.catsnap.domain.feed.service.FeedService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드 관련 API", description = "피드와 관련된 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;

    @Operation(summary = "피드를 작성하는 API(구현 완료)", description = "피드를 작성하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC001", description = "성공적으로 피드를 작성하였습니다.")
    })
    @LoginPhotographer
    @PostMapping
    public ResponseEntity<ResultResponse<FeedPhotoPresignedURLResponse>> postFeed(
        @UserId
        Long photographerId,
        @Parameter(description = "피드 작성 정보")
        @RequestBody
        FeedPostRequest feedPostRequest
    ) {
        FeedPhotoPresignedURLResponse response = feedService.postFeed(photographerId,
            feedPostRequest);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE, response);
    }

    @Operation(summary = "피드 1개를 피드 Id로 조회하는 API(구현 완료)", description = "피드 1개를 피드 Id로 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @AnyUser
    @GetMapping("/{feedId}")
    public ResponseEntity<ResultResponse<FeedDetailResponse>> getFeed(
        @PathVariable("feedId")
        Long feedId,
        @UserId
        Long userId
    ) {
        FeedDetailResponse feedDetailResponse = feedService.getFeedDetail(feedId, userId);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, feedDetailResponse);
    }

    @Operation(summary = "피드에 좋아요를 토글하는 API(구현 완료)", description = "피드의 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC001", description = "성공적으로 데이터를 생성했습니다.")
    })
    @LoginUser
    @PostMapping("/{feedId}/like")
    public ResponseEntity<ResultResponse<Void>> feedLikeToggle(
        @UserId
        Long userId,
        @PathVariable("feedId")
        Long feedId
    ) {
        feedLikeService.toggleFeedLike(feedId, userId);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE);
    }
}
