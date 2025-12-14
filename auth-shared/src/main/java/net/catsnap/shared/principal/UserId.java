package net.catsnap.shared.principal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러나 서비스 메서드의 파라미터에 사용되는 어노테이션으로, 현재 요청의 사용자 식별자(user id)를 주입하거나 표시하는 용도로 사용됩니다.
 *
 * <p>이 어노테이션은 파라미터 수준에서 사용되며 런타임에 유지됩니다.
 * 인증 인터셉터나 파라미터 리졸버에서 이 어노테이션이 붙은 파라미터에 현재 사용자 ID를 주입할 수 있습니다.</p>
 *
 * <p> 사용 예:
 * <pre>
 *     public Response getProfile(@UserId Long userId) { ... }
 * </pre>
 *
 * <p> <b>주의사항:</b>
 * <ul>
 *   <li>이 어노테이션 자체는 사용자 식별자 취득/검증 로직을 포함하지 않습니다. 실제 값 주입은 프레임워크의 파라미터 리졸버나 인터셉터에서 구현되어야 합니다.</li>
 *   <li>파라미터 타입(nullable/primitive 등)에 따라 추가 검증이 필요할 수 있습니다.</li>
 * </ul>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserId {

}
