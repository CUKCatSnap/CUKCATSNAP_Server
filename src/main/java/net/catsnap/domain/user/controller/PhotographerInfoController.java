package net.catsnap.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.user.photographer.dto.response.PhotographerFullyInformationResponse;
import net.catsnap.global.result.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사진작가 정보 API", description = "사진작가의 상세 정보를 조회하는 API입니다.")
@RestController
@RequestMapping("/photographer/information")
@RequiredArgsConstructor
public class PhotographerInfoController {

    @Operation(summary = "사진작가의 상세 정보를 조회하는 API", description = "사진작가의 상세 정보를 조회하는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SC000", description = "사진작가 자신의 환경설정 조회 성공")
    })
    @GetMapping("/{photographerId}")
    @AnyUser
    public ResponseEntity<ResultResponse<PhotographerFullyInformationResponse>> getPhotographerInfo(
        @PathVariable("photographerId") Long photographerId
    ) {
        return null;
    }
}
