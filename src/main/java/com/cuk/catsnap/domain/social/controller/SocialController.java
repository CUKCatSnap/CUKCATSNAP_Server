package com.cuk.catsnap.domain.social.controller;

import com.cuk.catsnap.domain.photographer.dto.PhotographerResponse;
import com.cuk.catsnap.domain.social.dto.SocialResponse;
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

import java.awt.print.Pageable;

@Tag(name="구독,차단 관련 API", description = "작가 구독, 장소 구독, 작가 차단에 관련된 API입니다.")
@RestController
@RequestMapping("/social")
public class SocialController {

    @Operation(summary = "구독한 작가 목록 또는 차단한 작가 목록 조회", description = "구독과 차단한 작가를 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS000", description = "성공적으로 구독 목록을 조회했습니다."),
    })
    @GetMapping("/{type}/photographer")

    public ResultResponse<PagedData<PhotographerResponse.PhotographerTinyInformationList>> getSubscribePhotographerList(
            @Parameter(description = "구독한 작가를 조회 : subscribe , 차단한 작가 조회: block")
            @PathVariable
            String type,
            @Parameter(hidden = true)
            Pageable pageable
    ){
        return null;
    }

    @Operation(summary = "구독한 장소 목록 조회", description = "구독한 장소의 목록을 조회하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SS000", description = "성공적으로 구독 목록을 조회했습니다."),
    })
    @GetMapping("/subscribe/place")
    public ResultResponse<PagedData<SocialResponse.subscribePlaceList>> getSubscribePlaceList(
            @Parameter(hidden = true)
            Pageable pageable
    ){
        return null;
    }

}
