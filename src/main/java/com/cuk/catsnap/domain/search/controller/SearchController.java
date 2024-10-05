package com.cuk.catsnap.domain.search.controller;

import com.cuk.catsnap.domain.search.dto.SearchRequest;
import com.cuk.catsnap.domain.search.dto.SearchResponse;
import com.cuk.catsnap.global.result.PagedData;
import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "검색을 담당하는 API입니다.", description = "모든 피드 검색과 리뷰 검색은 해당 api를 이용합니다.")
@RestController
@RequestMapping("/search")
public class SearchController {

    @Operation(summary = "피드를 상세검색 하는 API입니다. 결과물이", description = "피드를 상세검색하는 API. 모든 피드 검색은 해당 API이용. 상세 검색으로 사용되는 파라미터가 아니라면 null로 처리하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS000", description = "성공적으로 피드 검색을 완료하였습니다."),
    })
    @PostMapping("/feed/preview")
    public ResultResponse<PagedData<SearchResponse.PreviewFeedSearchList>> postPreviewFeedSearch(
            @Parameter(name="검색 파라미터", description = "검색 파라미터")
            @RequestBody
            SearchRequest.SearchParameter searchParameter,
            Pageable pageable
            ) {
        return null;
    }

    @Operation(summary = "피드를 상세검색 하는 API입니다. 더 많은 정보를 담는 피드 검색입니다.", description = "피드를 상세검색하는 API. 모든 피드 검색은 해당 API이용. 상세 검색으로 사용되는 파라미터가 아니라면 null로 처리하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS000", description = "성공적으로 피드 검색을 완료하였습니다."),
    })
    @PostMapping("/feed/detail")
    public ResultResponse<PagedData<SearchResponse.DetailFeedSearch>> postDetailFeedSearch(
            @Parameter(name="검색 파라미터", description = "검색 파라미터")
            @RequestBody
            SearchRequest.SearchParameter searchParameter,
            Pageable pageable
    ) {
        return null;
    }

    @Operation(summary = "리뷰를 상세검색 하는 API입니다.", description = "리뷰를 상세검색하는 API. 모든 리뷰 검색은 해당 API이용. 상세 검색으로 사용되는 파라미터가 아니라면 null로 처리하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS001", description = "성공적으로 리뷰 검색을 완료하였습니다."),
    })
    @PostMapping("/review/preview")
    public ResultResponse<PagedData<SearchResponse.PreviewReviewSearchList>> postPreviewReviewSearch(
            @Parameter(name="검색 파라미터", description = "검색 파라미터")
            @RequestBody
            SearchRequest.SearchParameter searchParameter,
            Pageable pageable
            ) {

        return null;
    }

    @Operation(summary = "리뷰를 상세검색 하는 API입니다. 더 많은 정보를 담는 리뷰 검색입니다.", description = "리뷰를 상세검색하는 API. 모든 리뷰 검색은 해당 API이용. 상세 검색으로 사용되는 파라미터가 아니라면 null로 처리하기")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS001", description = "성공적으로 리뷰 검색을 완료하였습니다."),
    })
    @PostMapping("/review/detail")
    public ResultResponse<PagedData<SearchResponse.DetailReviewSearchList>> postDetailReviewSearch(
            @Parameter(name="검색 파라미터", description = "검색 파라미터")
            @RequestBody
            SearchRequest.SearchParameter searchParameter,
            Pageable pageable
    ) {

        return null;
    }
}
