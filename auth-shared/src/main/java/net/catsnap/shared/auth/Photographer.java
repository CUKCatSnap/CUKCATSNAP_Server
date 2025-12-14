package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 포토그래퍼(Photographer) 권한 또는 역할을 표시하는 어노테이션입니다.
 *
 * <p>메서드 레벨에서 사용되며 런타임에 유지됩니다. 인터셉터나 권한 검사
 * 컴포넌트에서 이 어노테이션을 감지하여 호출자가 포토그래퍼 권한을 보유했는지 확인하는 데 사용됩니다.</p>
 * <p>
 * 사용 예:
 * <pre>
 *     {@code @Photographer}
 *     public void managePortfolio() { ... }
 * </pre>
 * <p>
 * 주의사항:
 * <ul>
 *   <li>이 어노테이션은 자체적으로 권한 검사를 수행하지 않습니다. 인가를 위해 별도의 로직(AOP, 필터 등)이 필요합니다.</li>
 *   <li>메서드 수준에서만 적용됩니다.</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Authentication
public @interface Photographer {

}
