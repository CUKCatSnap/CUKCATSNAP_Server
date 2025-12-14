package net.catsnap.shared.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 모델(photography model) 사용자 타입을 나타내는 어노테이션입니다.
 *
 * <p>메서드 레벨에서 사용되며 런타임에 유지됩니다. 주로 특정 API가 모델
 * 역할을 하는 사용자만 접근 가능해야 할 때 해당 메서드를 표시하는 용도로 사용합니다.</p>
 * <p>
 * 사용 예:
 * <pre>
 *     {@code @LoginModel}
 *     public void viewPortfolio() { ... }
 * </pre>
 * <p>
 * <b>주의사항:</b>
 * <ul>
 *   <li>이 어노테이션 자체는 권한 검사 로직을 포함하지 않으며, 별도의 인가 검사 컴포넌트가 필요합니다.</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Authentication
public @interface LonginModel {

}
