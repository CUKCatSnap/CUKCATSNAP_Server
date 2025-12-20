package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 다른 커스텀 어노테이션에 붙여 그 어노테이션이 "인증/인가(Authentication)"를 나타내는 메타 어노테이션입니다.
 *
 * <p>이 어노테이션은 다른 어노테이션(예: {@code @Admin}, {@code @Photographer})에
 * 붙여 사용합니다. 런타임에 유지되므로 AOP, 인터셉터, 필터 등에서 해당 어노테이션을 감지하여 인증/인가 검사를 중앙에서 수행할 수 있습니다.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {

}
