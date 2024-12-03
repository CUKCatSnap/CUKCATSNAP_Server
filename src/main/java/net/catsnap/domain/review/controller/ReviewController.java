package net.catsnap.domain.review.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.review.dto.Response.ReviewPhotoPresignedURLResponse;
import net.catsnap.domain.review.dto.request.PostReviewRequest;
import net.catsnap.domain.review.service.ReviewService;
import net.catsnap.domain.search.dto.response.ReviewSearchResponse;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.ReviewResultCode;
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
    public ResponseEntity<ResultResponse<ReviewPhotoPresignedURLResponse>> postReview(
        @Parameter(description = "새로운 리뷰를 만들 때 작성하는 형식입니다.")
        @RequestBody
        PostReviewRequest postReviewRequest
    ) {
        ReviewPhotoPresignedURLResponse dto = reviewService.postReview(postReviewRequest);
        return ResultResponse.of(ReviewResultCode.POST_REVIEW, dto);
    }

    @Operation(summary = "리뷰에 좋아요를 토글하는 API(구현 완료)", description = "리뷰에 좋아요를 토글하는 API입니다. 좋아요가 눌려있으면 취소하고, 눌려있지 않으면 좋아요를 누릅니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SV001", description = "리뷰 좋아요를 토글했습니다.")
    })
    @PostMapping("/like/{reviewId}")
    public ResponseEntity<ResultResponse<ReviewResultCode>> reviewLikeToggle(
        @Parameter(description = "리뷰 id")
        @RequestParam("reviewId")
        Long reviewId
    ) {
        reviewService.toggleReviewLike(reviewId);
        return ResultResponse.of(ReviewResultCode.REVIEW_LIKE_TOGGLE);
    }

    @Operation(summary = "리뷰 1개를 피드 Id로 조회하는 API(구현 완료)", description = "피드 1개를 피드 Id로 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SV002", description = "리뷰를 성공적으로 조회했습니다.")
    })
    @GetMapping("/{reviewId}")
    public ResponseEntity<ResultResponse<ReviewSearchResponse>> getReview(
        @Parameter(description = "리뷰 id")
        @PathVariable("reviewId")
        Long reviewId
    ) {
        ReviewSearchResponse reviewSearchResponse = reviewService.getReview(reviewId);
        return ResultResponse.of(ReviewResultCode.GET_REVIEW, reviewSearchResponse);
    }
}
