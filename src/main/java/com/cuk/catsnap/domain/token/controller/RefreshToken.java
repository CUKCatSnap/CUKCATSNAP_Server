package com.cuk.catsnap.domain.token.controller;

import com.cuk.catsnap.global.result.ResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "리프레시 토큰 발행", description = "리프레시 토큰을 발행하는 API입니다.")
@RestController
public class RefreshToken {

    /*
     * 해당 API는 작동하지 않습니다. API 명세만을 위한 것입니다.
     * 리프레시 토큰을 발행하는 API는 Spring Security의 필터에서 처리합니다.
     */
    @Operation(summary = "리프레시 토큰을 발행합니다.", description = "사용자와 작가 모두 같은 API를 사용합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200 SY001", description = "토큰 재발급을 성공적으로 수행"),
    })
    @GetMapping("/refresh/access-token")
    public ResponseEntity<ResultResponse<?>> signUp(
        @CookieValue(value = "refreshToken", required = true) String refreshToken
    ) {
        return null;
    }
}
