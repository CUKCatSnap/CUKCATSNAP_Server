package com.cuk.catsnap.domain.review.controller;

import com.cuk.catsnap.domain.review.dto.ReviewRequest;
import com.cuk.catsnap.domain.review.dto.ReviewResponse;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="리뷰에 관한 API", description = "리뷰에 관한 API입니다.")
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Operation(summary = "새로운 예약을 만드는 API", description = "새로운 예약을 만드는 API. 응답으로  AWS S3에 사진을 업로드 할 수 있는 URL을 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SV000", description = "새로운 리뷰를 업로드 했습니다.")
    })
    @PostMapping
    public ResultResponse<ReviewResponse.ReviewPhotoPresignedURL> postReview(
            @Parameter(description = "새로운 리뷰를 만들 때 작성하는 형식입니다.")
            @RequestBody
            ReviewRequest.PostReview postReview
    ){
        return null;
    }


}
