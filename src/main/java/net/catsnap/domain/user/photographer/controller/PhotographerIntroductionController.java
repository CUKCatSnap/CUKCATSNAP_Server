package net.catsnap.domain.user.photographer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.argumentresolver.UserId;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.interceptor.LoginPhotographer;
import net.catsnap.domain.user.photographer.dto.request.PhotographerIntroductionResponse;
import net.catsnap.domain.user.photographer.service.PhotographerIntroductionService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.CommonResultCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사진작가의 자기소개 관련 API", description = "사진작가의 자기소개를 관리하는 API입니다.")
@RestController
@RequestMapping("/photographer/introduction")
@RequiredArgsConstructor
public class PhotographerIntroductionController {

    private final PhotographerIntroductionService photographerIntroductionService;

    @Operation(summary = "작가가 자기소개를 수정하는 API(구현 완료)", description = "작가가 자기소개를 수정하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC002", description = "성공적으로 데이터를 수정했습니다. ")
    })
    @PostMapping("/my")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<Void>> setMyIntroduction(
        @UserId
        Long photographerId,
        @RequestParam("introduction") String introduction
    ) {
        photographerIntroductionService.updatePhotographerIntroduction(photographerId,
            introduction);
        return ResultResponse.of(CommonResultCode.COMMON_REVISE);
    }

    @Operation(summary = "작가가 자기소개를 조회하는 API(구현 완료)", description = "작가가 자기소개를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @GetMapping("/my")
    @LoginPhotographer
    public ResponseEntity<ResultResponse<PhotographerIntroductionResponse>> getMyIntroduction(
        @UserId
        Long photographerId
    ) {
        PhotographerIntroductionResponse response = photographerIntroductionService.getPhotographerIntroduction(
            photographerId);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, response);
    }

    @Operation(summary = "사용자가 작가의 자기소개를 조회하는 API(구현 완료)", description = "사용자가 작가가 자기소개를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "성공적으로 데이터를 조회했습니다.")
    })
    @GetMapping("/{photographerId}")
    @AnyUser
    public ResponseEntity<ResultResponse<PhotographerIntroductionResponse>> getPhotographerIntroduction(
        @PathVariable("photographerId")
        Long photographerId
    ) {
        PhotographerIntroductionResponse response = photographerIntroductionService.getPhotographerIntroduction(
            photographerId);
        return ResultResponse.of(CommonResultCode.COMMON_LOOK_UP, response);
    }
}
