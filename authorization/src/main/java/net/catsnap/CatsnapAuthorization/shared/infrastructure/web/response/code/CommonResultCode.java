package net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapAuthorization.shared.infrastructure.web.response.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 공통 결과 코드
 * <p>
 * 모든 도메인에서 공통적으로 사용하는 성공 응답 코드를 정의합니다.
 */
@Getter
@RequiredArgsConstructor
public enum CommonResultCode implements ResultCode {

    /**
     * 데이터 조회 성공
     */
    COMMON_READ(HttpStatus.OK, "SA000", "성공적으로 데이터를 조회했습니다."),

    /**
     * 데이터 생성 성공
     */
    COMMON_CREATE(HttpStatus.CREATED, "SA001", "성공적으로 데이터를 생성했습니다."),

    /**
     * 데이터 수정 성공
     */
    COMMON_UPDATE(HttpStatus.OK, "SA002", "성공적으로 데이터를 수정했습니다."),

    /**
     * 데이터 삭제 성공
     */
    COMMON_DELETE(HttpStatus.OK, "SA003", "성공적으로 데이터를 삭제했습니다."),

    /**
     * 작업 완료 (데이터 없음)
     */
    COMMON_NO_CONTENT(HttpStatus.NO_CONTENT, "SA004", "요청이 성공적으로 처리되었습니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}
