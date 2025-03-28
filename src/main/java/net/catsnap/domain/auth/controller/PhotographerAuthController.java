package net.catsnap.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.auth.dto.photographer.request.PhotographerSignUpRequest;
import net.catsnap.domain.auth.interceptor.AnyUser;
import net.catsnap.domain.auth.service.PhotographerAuthService;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.result.code.PhotographerResultCode;
import net.catsnap.global.security.dto.AccessTokenResponse;
import net.catsnap.global.security.dto.SecurityRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사진작가의 로그인 관련 API", description = "사진작가의 회원가입, 로그인 관련 API입니다")
@RestController
@RequestMapping("/photographer")
@RequiredArgsConstructor
public class PhotographerAuthController {

    private final PhotographerAuthService photographerAuthService;

    @Operation(summary = "작가 회원가입 API(구현 완료)", description = "작가가 회원가입을 할 수 있는 API입니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201 SP000", description = "사진작가 회원가입 성공."),
        @ApiResponse(responseCode = "409 EP000", description = "중복된 ID로 회원가입이 불가능 합니다.")
    })
    @PostMapping("/signup/catsnap")
    @AnyUser
    public ResponseEntity<ResultResponse<PhotographerResultCode>> signUp(
        @Parameter(description = "회원가입 양식", required = true)
        @RequestBody
        PhotographerSignUpRequest photographerSignUpRequest
    ) {
        photographerAuthService.singUp(photographerSignUpRequest);
        return ResultResponse.of(PhotographerResultCode.PHOTOGRAPHER_SIGN_UP);
    }

    /*
     *로그인 처리는 Spring Security의 필터로 처리하므로 해당 메서드는 필요하지 않습니다.
     *해당 컨트롤러는 API 명세만을 위한 것입니다.
     */
    @Operation(summary = "자체 서비스 API(구현 완료)", description = "작가 로그인은 자체 로그인만 지원합니다. 네이버 로그인 지원 안함.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SY000", description = "로그인 성공"),
        @ApiResponse(responseCode = "401 EY000", description = "로그인 실패 (아이디 또는 비밀번호가 일치하지 않음)"),
        @ApiResponse(responseCode = "400 EY001", description = "로그인 실패 (잘못된 로그인 API 요청 형식)"),
    })
    @PostMapping("/signin/catsnap")
    public ResponseEntity<ResultResponse<AccessTokenResponse>> signIn(
        @Parameter(description = "로그인 양식", required = true)
        @RequestBody
        SecurityRequest.CatsnapSignInRequest photographerSignIn
    ) {
        return null;
    }
}
