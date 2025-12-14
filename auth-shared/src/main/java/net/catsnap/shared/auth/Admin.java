package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 관리자 권한이 필요한 메서드임을 표시하는 어노테이션입니다.
 *
 * <p>이 어노테이션은 메서드 레벨에서 사용되며 런타임에 유지됩니다.
 * 인증/인가(interceptor 또는 필터) 계층에서 해당 메서드를 호출하려는 사용자가 관리자 권한을 보유했는지 확인하는 용도로 사용합니다.</p>
 * <p>
 * 사용 예:
 * <pre>
 *     {@code @Admin}
 *     public void deleteUser(Long userId) { ... }
 * </pre>
 * <p>
 * 주의사항: - 이 어노테이션 자체는 권한 검사 로직을 포함하지 않으며, 런타임에서 이를 감지하고 검사하는 별도의 컴포넌트(예: AOP 어드바이저, 서블릿 필터, 인터셉터) 가
 * 필요합니다. - 메서드 수준에서만 적용되며 클래스 수준에 적용하려면 별도 어노테이션이나 정책이 필요합니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Admin {

}
