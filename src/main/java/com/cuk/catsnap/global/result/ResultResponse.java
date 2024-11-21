package com.cuk.catsnap.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
public class ResultResponse<T> {

    @Schema(description = "HTTP 상태코드를 의미합니다.")
    private final int status;

    @Schema(description = "HTTP 상태코드보다 더 자세한 상태코드를 의미합니다")
    private final String code;

    @Schema(description = "HTTP 상태코드보다 더 자세한 상태코드를 의미합니다")
    private final String message;

    @Schema(description = "api의 데이터 부분입니다.")
    private final T data;

    public static <T> ResponseEntity<ResultResponse<T>> of(ResultCode resultCode, T data) {
        return ResponseEntity
            .status(resultCode.getStatus())
            .body(ResultResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build());
    }

    public static <T> ResponseEntity<ResultResponse<T>> of(ResultCode resultCode) {
        return ResponseEntity
            .status(resultCode.getStatus())
            .body(ResultResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(null)
                .build());
    }

    /*
     * 스프링 컨테이너 내부에서 응답하지 않는 경우에 사용하는 메서드 (ex : 시큐리티 필터)
     */
    public static <T> ResultResponse<T> ofNotEntity(ResultCode resultCode) {
        return ResultResponse.<T>builder()
            .status(resultCode.getStatus())
            .code(resultCode.getCode())
            .message(resultCode.getMessage())
            .data(null)
            .build();
    }
}
