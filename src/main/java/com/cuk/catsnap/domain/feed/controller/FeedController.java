package com.cuk.catsnap.domain.feed.controller;

import com.cuk.catsnap.domain.feed.dto.FeedResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
