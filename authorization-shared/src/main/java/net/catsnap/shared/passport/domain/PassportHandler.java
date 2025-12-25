package net.catsnap.shared.passport.domain;

import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;
import net.catsnap.shared.passport.domain.exception.InvalidPassportException;
import net.catsnap.shared.passport.domain.exception.PassportParsingException;

/**
 * Passport 발급 및 파싱을 담당하는 도메인 인터페이스. 바이트 기반 서명 및 검증을 통해 내부 서비스 간 안전한 인증 정보 전달을 제공합니다.
 *
 * <p>구현체는 다음을 보장해야 합니다:
 * <ul>
 *   <li>서명 검증을 통한 위변조 방지</li>
 *   <li>만료 시간 검증</li>
 *   <li>일관된 직렬화/역직렬화</li>
 * </ul>
 */
public interface PassportHandler {

    String PassportKey = "X-Passport";

    /**
     * Passport를 서명하여 base64 인코딩된 문자열로 변환합니다.
     *
     * @param passport 서명할 Passport 객체
     * @return base64 인코딩된 서명된 passport 문자열
     * @throws IllegalArgumentException passport가 null이거나 유효하지 않은 경우
     */
    String sign(Passport passport);

    /**
     * 서명된 passport 문자열을 파싱하고 검증합니다.
     *
     * @param signedPassport base64 인코딩된 서명된 passport 문자열
     * @return 검증된 Passport 객체
     * @throws PassportParsingException  Passport 파싱에 실패한 경우 (Base64 디코딩 실패, 잘못된 형식 등)
     * @throws InvalidPassportException  Passport 서명 검증에 실패한 경우
     * @throws ExpiredPassportException  Passport가 만료된 경우
     */
    Passport parse(String signedPassport);
}
