package net.catsnap.CatsnapReservation.shared.presentation.response;

import lombok.Builder;
import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.springframework.http.ResponseEntity;

/**
 * API 공통 응답 포맷
 * <p>
 * HTTP 상태 코드는 ResponseEntity의 헤더에 설정되며,
 * 응답 body에는 비즈니스 상태 코드(code)와 메시지, 데이터만 포함됩니다.
 * </p>
 */
@Builder
@Getter
public class ResultResponse<T> {

    private final String code;

    private final String message;

    private final T data;

    /**
     * 데이터를 포함한 응답을 생성합니다.
     *
     * @param resultCode 결과 코드
     * @param data       응답 데이터
     * @return ResponseEntity로 래핑된 응답
     */
    public static <T> ResponseEntity<ResultResponse<T>> of(ResultCode resultCode, T data) {
        return ResponseEntity
            .status(resultCode.getHttpStatus())
            .body(ResultResponse.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build());
    }

    /**
     * 데이터가 없는 응답을 생성합니다.
     *
     * @param resultCode 결과 코드
     * @return ResponseEntity로 래핑된 응답
     */
    public static <T> ResponseEntity<ResultResponse<T>> of(ResultCode resultCode) {
        return of(resultCode, null);
    }
}
