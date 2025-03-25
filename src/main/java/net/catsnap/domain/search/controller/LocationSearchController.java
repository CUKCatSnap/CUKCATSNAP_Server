package net.catsnap.domain.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.search.dto.request.LocationSearchRequest;
import net.catsnap.domain.search.dto.response.LocationSearchListResponse;
import net.catsnap.global.result.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위치 기반으로 리뷰를 조회하는 API", description = "위치 기반으로 리뷰를 조회하는 API입니다.")
@RestController
@RequestMapping("/location/search")
public class LocationSearchController {

    @Operation(summary = "리뷰 1개를 피드 Id로 조회하는 API(구현 완료)", description = "피드 1개를 피드 Id로 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @PostMapping
    @AnyUser
    public ResponseEntity<ResultResponse<LocationSearchListResponse>> getLocationReview(
        @RequestBody
        LocationSearchRequest locationSearchRequest
    ) {
        return null;
    }
}
