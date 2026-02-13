package net.catsnap.CatsnapReservation.shared.domain.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.CatsnapReservation.shared.ResultCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * 도메인 에러 코드
 * <p>
 * 비즈니스 규칙 위반 시 발생하는 에러를 정의합니다.
 * 예약 중복, 불가능한 시간대, 이미 취소된 예약 등 도메인 로직 관련 에러를 담당합니다.
 * </p>
 */
@Getter
@RequiredArgsConstructor
public enum DomainErrorCode implements ResultCode {

    /**
     * 도메인 제약 조건 위반
     * <p>
     * 도메인 로직 상 허용되지 않는 값이나 상태인 경우
     * </p>
     */
    DOMAIN_CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "ED000", "해당 값이 유효하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "ED001", "리소스를 찾을 수 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "ED002", "리소스가 충돌합니다.");

    private final HttpStatusCode httpStatus;
    private final String code;
    private final String message;
}
