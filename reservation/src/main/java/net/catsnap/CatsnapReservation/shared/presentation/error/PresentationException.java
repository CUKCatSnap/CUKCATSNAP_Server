package net.catsnap.CatsnapReservation.shared.presentation.error;

import lombok.Getter;
import net.catsnap.CatsnapReservation.shared.ResultCode;

/**
 * 프레젠테이션 예외 최상위 클래스
 * <p>
 * 프레젠테이션 레이어에서 발생하는 모든 예외의 기본 클래스입니다.
 * API 요청 형식 오류, 인증/인가 실패 등의 예외를 포함합니다.
 * ResultCode를 포함하여 일관된 응답 형식을 제공합니다.
 * </p>
 */
@Getter
public class PresentationException extends RuntimeException {

    private final ResultCode resultCode;

    /**
     * ResultCode를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     */
    public PresentationException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 원인 예외를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode 응답에 사용할 결과 코드
     * @param cause      원인 예외
     */
    public PresentationException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.resultCode = resultCode;
    }

    /**
     * ResultCode와 추가 메시지를 포함한 비즈니스 예외를 생성합니다.
     *
     * @param resultCode        응답에 사용할 결과 코드
     * @param additionalMessage 추가 메시지
     */
    public PresentationException(ResultCode resultCode, String additionalMessage) {
        super(resultCode.getMessage() + " - " + additionalMessage);
        this.resultCode = resultCode;
    }
}