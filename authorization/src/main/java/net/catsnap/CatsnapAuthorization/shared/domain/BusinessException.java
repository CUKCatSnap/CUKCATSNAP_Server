package net.catsnap.CatsnapAuthorization.shared.domain;

import lombok.Getter;
import net.catsnap.CatsnapAuthorization.shared.domain.error.ResultCode;

/**
 * 비즈니스 예외 최상위 클래스
 * <p>
 * 비즈니스 로직 실행 중 발생하는 모든 예외의 기본 클래스입니다. ResultCode를 포함하여 일관된 응답 형식을 제공합니다.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ResultCode resultCode;

    /**
     * ResultCode를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 원인 예외를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     * @param cause      원인 예외
     */
    public BusinessException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 추가 메시지를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode        응답에 사용할 결과 코드
     * @param additionalMessage 추가 메시지
     */
    public BusinessException(ResultCode resultCode, String additionalMessage) {
        super(resultCode.getMessage() + " - " + additionalMessage);
        this.resultCode = resultCode;
    }
}