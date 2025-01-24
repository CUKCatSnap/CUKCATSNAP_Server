package net.catsnap.domain.auth.argumentresolver;

import net.catsnap.domain.auth.annotation.AnyUser;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AnyUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.hasParameterAnnotation(AnyUser.class);
        boolean hasToken =
            GetAuthenticationInfo.getAuthority() == CatsnapAuthority.MEMBER
                || GetAuthenticationInfo.getAuthority() == CatsnapAuthority.PHOTOGRAPHER
                || GetAuthenticationInfo.getAuthority() == CatsnapAuthority.ANONYMOUS;
        return isLoginMemberAnnotation && hasToken;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (GetAuthenticationInfo.getAuthority() == CatsnapAuthority.ANONYMOUS) {
            /*
             * 추후 처리
             */
            return null;
        } else {
            return GetAuthenticationInfo.getUserId();
        }
    }
}
