package net.catsnap.CatsnapReservation.shared.presentation.web.resolver;

import jakarta.servlet.http.HttpServletRequest;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationException;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;
import net.catsnap.shared.passport.domain.exception.InvalidPassportException;
import net.catsnap.shared.passport.domain.exception.PassportParsingException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * {@link UserId} 어노테이션이 붙은 파라미터에 사용자 ID를 주입하는 ArgumentResolver입니다.
 * <p>
 * 게이트웨이에서 발급한 Passport 헤더(X-Passport)를 파싱하여 사용자 ID를 추출합니다.
 * </p>
 *
 * @see UserId
 * @see PassportHandler
 */
@Component
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final PassportHandler passportHandler;

    public UserIdArgumentResolver(PassportHandler passportHandler) {
        this.passportHandler = passportHandler;
    }

    /**
     * 이 ArgumentResolver가 해당 파라미터를 처리할 수 있는지 확인합니다.
     *
     * @param parameter 메서드 파라미터
     * @return {@link UserId} 어노테이션이 있으면 true
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class);
    }

    /**
     * Passport에서 사용자 ID를 추출하여 반환합니다.
     *
     * @param parameter     메서드 파라미터
     * @param mavContainer  ModelAndViewContainer
     * @param webRequest    웹 요청
     * @param binderFactory WebDataBinderFactory
     * @return 사용자 ID (Long)
     * @throws PresentationException Passport가 없거나 유효하지 않은 경우
     */
    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new PresentationException(PresentationErrorCode.UNAUTHORIZED);
        }

        String signedPassport = request.getHeader(PassportHandler.PASSPORT_KEY);
        if (signedPassport == null || signedPassport.isBlank()) {
            throw new PresentationException(PresentationErrorCode.UNAUTHORIZED);
        }

        try {
            Passport passport = passportHandler.parse(signedPassport);
            return passport.userId();
        } catch (PassportParsingException | InvalidPassportException e) {
            throw new PresentationException(PresentationErrorCode.INVALID_PASSPORT);
        } catch (ExpiredPassportException e) {
            throw new PresentationException(PresentationErrorCode.EXPIRED_PASSPORT);
        }
    }
}
