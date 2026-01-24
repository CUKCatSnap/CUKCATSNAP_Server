package net.catsnap.CatsnapReservation.shared.domain.error;

import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.ResultCode;

/**
 * 도메인 예외 최상위 클래스
 * <p>
 * 도메인 레이어에서 비즈니스 규칙 위반 시 발생하는 모든 예외의 기본 클래스입니다.
 * ResultCode를 포함하여 일관된 응답 형식을 제공합니다.
 * </p>
 */
@Getter
public class DomainException extends RuntimeException {

    private final ResultCode resultCode;

    /**
     * ResultCode를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     */
    public DomainException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 원인 예외를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     * @param cause      원인 예외
     */
    public DomainException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 추가 메시지를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode        응답에 사용할 결과 코드
     * @param additionalMessage 추가 메시지
     */
    public DomainException(ResultCode resultCode, String additionalMessage) {
        super(resultCode.getMessage() + " - " + additionalMessage);
        this.resultCode = resultCode;
    }
}