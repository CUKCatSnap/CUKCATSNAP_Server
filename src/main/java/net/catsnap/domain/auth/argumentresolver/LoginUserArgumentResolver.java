package net.catsnap.domain.auth.argumentresolver;

import net.catsnap.domain.auth.annotation.LoginUser;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isLoginMemberAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean isLoginUser =
            GetAuthenticationInfo.getAuthority() == CatsnapAuthority.MEMBER
                || GetAuthenticationInfo.getAuthority() == CatsnapAuthority.PHOTOGRAPHER;
        return isLoginMemberAnnotation && isLoginUser;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return GetAuthenticationInfo.getUserId();
    }
}
