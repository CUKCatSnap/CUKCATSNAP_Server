package com.cuk.catsnap.global.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

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

    public static <T> ResultResponse<T> of(ResultCode resultCode, T data) {
        return ResultResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ResultResponse<T> of(ResultCode resultCode) {
        return ResultResponse.<T>builder()
                .status(resultCode.getStatus())
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(null)
                .build();
    }
}
