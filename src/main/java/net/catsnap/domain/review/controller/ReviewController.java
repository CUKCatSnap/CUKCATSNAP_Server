package net.catsnap.domain.review.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginMember;
import net.catsnap.domain.auth.interceptor.LoginUser;
import net.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import net.catsnap.domain.review.dto.request.PostReviewRequest;
import net.catsnap.domain.review.service.ReviewService;
import net.catsnap.domain.search.dto.response.ReviewSearchListResponse;
import net.catsnap.domain.search.dto.response.ReviewSearchResponse;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.SlicedData;
import net.catsnap.global.result.code.CommonResultCode;
import net.catsnap.global.result.code.ReviewResultCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리뷰에 관한 API", description = "리뷰에 관한 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰를 작성하는 API(구현 완료)", description = "리뷰를 작성하는 API. 응답으로  AWS S3에 사진을 업로드 할 수 있는 URL을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SV000", description = "새로운 리뷰를 업로드 했습니다.")
    })
    @PostMapping
    @LoginMember
    public ResponseEntity<ResultResponse<ReviewPhotoPresignedURLResponse>> postReview(
        @Parameter(description = "새로운 리뷰를 만들 때 작성하는 형식입니다.")
        @RequestBody
        PostReviewRequest postReviewRequest
    ) {
        ReviewPhotoPresignedURLResponse dto = reviewService.postReview(postReviewRequest);
        return ResultResponse.of(ReviewResultCode.POST_REVIEW, dto);
    }

    @Operation(summary = "나의 리뷰 목록을 조회하는 API(구현 완료)", description = "나의 리뷰 목록을 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @GetMapping("/my/all")
    @LoginUser
    public ResponseEntity<ResultResponse<SlicedData<ReviewSearchListResponse>>> getMyReview(
        @UserId
        Long userId,
        Pageable pageable
    ) {
        SlicedData<ReviewSearchListResponse> reviewSearchListResponse = reviewService.getMyReview(
            userId, pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, reviewSearchListResponse);
    }

    @Operation(summary = "특정 작가의 리뷰를 조회하는 API(구현 완료)", description = "특정 작가의 리뷰를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @GetMapping
    @AnyUser
    public ResponseEntity<ResultResponse<SlicedData<ReviewSearchListResponse>>> getPhotographerReview(
        @Parameter(description = "작가 id")
        @RequestParam("photographerId")
        Long photographerId,
        @UserId
        Long userId,
        @PageableDefault(size = 10, sort = "createdAt")
        Pageable pageable
    ) {
        SlicedData<ReviewSearchListResponse> reviewSearchListResponse
            = reviewService.getPhotographerReview(photographerId, userId, pageable);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, reviewSearchListResponse);
    }

    @Operation(summary = "리뷰에 좋아요를 토글하는 API(구현 완료)", description = "리뷰에 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SV001", description = "리뷰 좋아요를 토글했습니다.")
    })
    @PostMapping("/like/{reviewId}")
    @LoginUser
    public ResponseEntity<ResultResponse<ReviewResultCode>> reviewLikeToggle(
        @UserId
        Long userId,
        @Parameter(description = "리뷰 id")
        @PathVariable("reviewId")
        Long reviewId
    ) {
        reviewService.toggleReviewLike(reviewId, userId);
        return ResultResponse.of(ReviewResultCode.REVIEW_LIKE_TOGGLE);
    }

    @Operation(summary = "리뷰 1개를 피드 Id로 조회하는 API(구현 완료)", description = "피드 1개를 피드 Id로 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SV002", description = "리뷰를 성공적으로 조회했습니다.")
    })
    @GetMapping("/{reviewId}")
    @AnyUser
    public ResponseEntity<ResultResponse<ReviewSearchResponse>> getReview(
        @UserId
        Long userId,
        @Parameter(description = "리뷰 id")
        @PathVariable("reviewId")
        Long reviewId
    ) {
        ReviewSearchResponse reviewSearchResponse = reviewService.getReview(reviewId, userId);
        return ResultResponse.of(ReviewResultCode.GET_REVIEW, reviewSearchResponse);
    }
}
