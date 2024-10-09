package com.cuk.catsnap.domain.member.controller;

import com.cuk.catsnap.domain.member.dto.MemberRequest;
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

@Tag(name = "회원(모델) 관련 API", description = "회원(모델)을 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/member")
public class MemberController {

    @Operation(summary = "회원가입 API", description = "회원가입을 할 수 있는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201 SM000", description = "성공적으로 회원가입을 했습니다.")
    })
    @PostMapping("/signup/catsnsap")
    public ResultResponse<?> signUp(
            @Parameter(description = "회원가입 양식", required = true)
            @RequestBody
            MemberRequest.MemberSignUp memberSignUp
    ) {
        return null;
    }
}
