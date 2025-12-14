package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 모든 사용자가 접근 가능한 메서드임을 표시하는 어노테이션입니다.
 *
 * <p>이 어노테이션은 메서드 수준에서 사용되며 런타임에 유지됩니다. 인터셉터,
 * AOP, 파라미터 리졸버 등에서 이 어노테이션을 감지해 현재 요청자가 인증된 사용자임을 확인하거나, 인증된 사용자 정보를 주입하는 데 사용할 수 있습니다.</p>
 * <p>
 * 사용 예:
 * <pre>
 *     {@code @AnyUser}
 *     public Response getMyProfile() { ... }
 * </pre>
 * <p>
 * 주의사항: - 이 어노테이션 자체는 인증/인가 로직을 포함하지 않습니다. 실제 검사와 사용자 정보 주입은 별도의 런타임 컴포넌트(예: 인터셉터 또는 AOP)에서 구현되어야
 * 합니다.
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Authentication
public @interface AnyUser {

}
