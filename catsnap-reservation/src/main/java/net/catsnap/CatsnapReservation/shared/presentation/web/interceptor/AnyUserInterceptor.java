package net.catsnap.CatsnapReservation.shared.presentation.web.interceptor;

import java.util.List;
import net.catsnap.shared.auth.AnyUser;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.springframework.stereotype.Component;

/**
 * 모든 사용자 접근 허용 인터셉터입니다.
 * <p>
 * {@link AnyUser} 어노테이션이 붙은 컨트롤러 메서드에 대해 모든 권한의 접근을 허용합니다. 익명 사용자를 포함한 모든 사용자가 접근할 수 있지만, 반드시 인증
 * 헤더는 필요합니다.
 * </p>
 *
 * <h3>허용 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#ANONYMOUS} - 익명 사용자</li>
 *   <li>{@link CatsnapAuthority#MODEL} - 모델</li>
 *   <li>{@link CatsnapAuthority#PHOTOGRAPHER} - 사진작가</li>
 *   <li>{@link CatsnapAuthority#ADMIN} - 관리자</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class PhotoController {
 *
 *     @AnyUser // 모든 인증된 사용자 접근 가능
 *     @GetMapping("/photos/{id}")
 *     public PhotoResponse getPhoto(@PathVariable Long id) {
 *         // 익명 사용자 포함 모든 사용자가 사진 조회 가능
 *     }
 * }
 * }</pre>
 *
 * @see AnyUser
 * @see AbstractAuthInterceptor
 */
@Component
public class AnyUserInterceptor extends AbstractAuthInterceptor<AnyUser> {

    public AnyUserInterceptor(PassportHandler passportHandler) {
        super(AnyUser.class, List.of(
            CatsnapAuthority.ANONYMOUS,
            CatsnapAuthority.MODEL,
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ), passportHandler);
    }
}
