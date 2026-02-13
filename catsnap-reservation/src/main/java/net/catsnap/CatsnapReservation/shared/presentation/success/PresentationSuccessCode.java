package net.catsnap.CatsnapReservation.shared.presentation.success;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 프레젠테이션 레이어 성공 코드
 * <p>
 * API 요청 처리 성공 시 사용하는 공통 응답 코드를 정의합니다.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum PresentationSuccessCode implements ResultCode {

    /**
     * 데이터 조회 성공
     */
    READ(HttpStatus.OK, "SA000", "성공적으로 데이터를 조회했습니다."),

    /**
     * 데이터 생성 성공
     */
    CREATE(HttpStatus.CREATED, "SA001", "성공적으로 데이터를 생성했습니다."),

    /**
     * 데이터 수정 성공
     */
    UPDATE(HttpStatus.OK, "SA002", "성공적으로 데이터를 수정했습니다."),

    /**
     * 데이터 삭제 성공
     */
    DELETE(HttpStatus.OK, "SA003", "성공적으로 데이터를 삭제했습니다."),

    /**
     * 작업 완료 (데이터 없음)
     */
    NO_CONTENT(HttpStatus.NO_CONTENT, "SA004", "요청이 성공적으로 처리되었습니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}
