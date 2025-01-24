package net.catsnap.domain.auth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * 로그인 한 사용자와 로그인 하지 않은 사용자 모두를 대상으로 하는 컨트롤러 메소드 파라미터
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnyUser {

}
