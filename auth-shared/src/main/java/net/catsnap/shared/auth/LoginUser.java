package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 한 사용자를 나타내는(또는 로그인 필요를 표시하는) 어노테이션입니다.
 *
 * <p>메서드 레벨에서 사용되며 런타임에 유지됩니다. 인터셉터나 필터에서 이
 * 어노테이션을 감지하여 현재 호출자가 인증된 사용자(로그인 사용자)인지 확인하거나, 로그인 사용자 정보를 주입하는 용도로 활용할 수 있습니다.</p>
 * <p>
 * 사용 예:
 * <pre>
 *     {@code @LoginUser}
 *     public UserProfile getProfile() { ... }
 * </pre>
 * <p>
 * 주의사항: - 이 어노테이션 자체는 인증 로직을 수행하지 않습니다. 인증/인가 로직은 별도의 구성요소에서 구현되어야 합니다. - 파라미터 주입(annotation-based
 * parameter resolver)을 사용하는 경우, 해당 프레임워크의 컨벤션에 맞게 추가 설정이 필요할 수 있습니다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

}
