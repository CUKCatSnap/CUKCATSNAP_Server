package net.catsnap.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.security.dto.AccessTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원(모델)의 로그인 관련 API", description = "회원(모델)의 회원가입, 로그인 관련 API입니다")
@RestController
@RequestMapping()
@RequiredArgsConstructor
public class MemberAuthController {

    @Operation(summary = "네이버 소셜로그인 API(구현 완료)",
        description = """
            네이버 소셜로그인으로 회원가입과 로그인을 할 수 있는 API입니다.
            로그인 성공 시 리프레시 토큰을 쿠키에 담아서 반환합니다.
            """
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201 SM000", description = "성공적으로 회원가입(로그인)을 했습니다."),
    })
    @PostMapping("/oauth2/authorization/naver")
    public ResponseEntity<ResultResponse<AccessTokenResponse>> oAuthSignUp(
    ) {
        return null;
    }
}
