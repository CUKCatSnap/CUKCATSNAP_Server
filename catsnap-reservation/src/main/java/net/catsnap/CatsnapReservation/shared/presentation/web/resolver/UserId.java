package net.catsnap.CatsnapReservation.shared.presentation.web.resolver;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 컨트롤러 메서드 파라미터에 현재 인증된 사용자의 ID를 주입하는 어노테이션입니다.
 * <p>
 * 이 어노테이션이 붙은 {@code Long} 타입 파라미터에 Passport에서 추출한 사용자 ID가 주입됩니다.
 * 반드시 인증된 요청에서만 사용해야 합니다 ({@code @LoginPhotographer}, {@code @LoginModel} 등과 함께 사용).
 * </p>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class ProgramController {
 *
 *     @LoginPhotographer
 *     @PostMapping("/programs")
 *     public ProgramResponse createProgram(
 *         @UserId Long photographerId,  // Passport에서 userId 추출
 *         @RequestBody ProgramCreateRequest request
 *     ) {
 *         return programService.createProgram(photographerId, request);
 *     }
 * }
 * }</pre>
 *
 * @see UserIdArgumentResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserId {

}
