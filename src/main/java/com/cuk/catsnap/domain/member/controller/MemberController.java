package com.cuk.catsnap.domain.member.controller;

import com.cuk.catsnap.domain.member.dto.MemberRequest;
import com.cuk.catsnap.domain.member.service.MemberService;
import com.cuk.catsnap.global.result.ResultResponse;
import com.cuk.catsnap.global.result.code.MemberResultCode;
import com.cuk.catsnap.global.security.dto.SecurityRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원(모델) 관련 API", description = "회원(모델)을 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

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
        memberService.singUp(memberSignUp);
        return ResultResponse.of(MemberResultCode.MEMBER_SIGN_UP);
    }

    /*
    *로그인 처리는 Spring Security의 필터로 처리하므로 해당 메서드는 필요하지 않습니다.
    *해당 컨트롤러는 API 명세만을 위한 것입니다.
     */

    @Operation(summary = "자체 서비스 API", description = "자체 서비스 로그인(네이버나 카카오 등의 OAuth 로그인이 아닌 " +
            "자체 서비스 로그인)을 할 수 있는 API입니다. 로그인 성공 시 헤더에 accessToken: Bearer {accessToken}과 " +
            "쿠키에 refreshToken: {refreshToken}을 담아서 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200 SM000", description = "로그인 성공"),
    })
    @PostMapping("/signin/catsnap")
    public ResultResponse<?> signIn(
            @Parameter(description = "로그인 양식", required = true)
            @RequestBody
            SecurityRequest.MemberSingInRequest memberSignIn
    ) {

        return null;
    }
}
