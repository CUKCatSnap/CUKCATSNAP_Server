package com.cuk.catsnap.domain.photographer.controller;

import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.photographer.dto.PhotographerRequest;
import com.cuk.catsnap.domain.photographer.service.PhotographerService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.MemberResultCode;
import com.cuk.catsnap.global.result.code.PhotographerResultCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photographer")
@RequiredArgsConstructor
public class PhotographerController {

    private final PhotographerService photographerService;

    @Operation(summary = "작가 회원가입 API(구현 완료)", description = "작가가 회원가입을 할 수 있는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SP000", description = "사진작가 회원가입 성공."),
            @ApiResponse(responseCode = "409 EP000", description = "중복된 ID로 회원가입이 불가능 합니다.")
    })
    @PostMapping("/signup/catsnsap")
    public ResultResponse<?> signUp(
            @Parameter(description = "회원가입 양식", required = true)
            @RequestBody
            PhotographerRequest.PhotographerSignUp photographerSignUp
    ) {
        photographerService.singUp(photographerSignUp);
        return ResultResponse.of(PhotographerResultCode.PHOTOGRAPHER_SIGN_UP);
    }
}
