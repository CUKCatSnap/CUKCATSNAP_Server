package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginUser;
import org.springframework.stereotype.Component;

/**
 * 로그인한 사용자 권한 검증 인터셉터입니다.
 * <p>
 * {@link LoginUser} 어노테이션이 붙은 컨트롤러 메서드에 대해 로그인한 사용자만 접근을 허용합니다. 익명(ANONYMOUS) 사용자는 접근할 수 없으며, 실제
 * 계정을 가진 사용자만 접근 가능합니다.
 * </p>
 *
 * <h3>허용 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#MODEL} - 모델</li>
 *   <li>{@link CatsnapAuthority#PHOTOGRAPHER} - 사진작가</li>
 *   <li>{@link CatsnapAuthority#ADMIN} - 관리자</li>
 * </ul>
 *
 * <h3>거부 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#ANONYMOUS} - 익명 사용자</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class ProfileController {
 *
 *     @LoginUser // 로그인한 사용자만 접근 가능
 *     @GetMapping("/profile")
 *     public ProfileResponse getMyProfile() {
 *         // 모델, 사진작가, 관리자 모두 자신의 프로필 조회 가능
 *     }
 * }
 * }</pre>
 *
 * @see LoginUser
 * @see AbstractAuthInterceptor
 */
@Component
public class LoginUserInterceptor extends AbstractAuthInterceptor<LoginUser> {

    public LoginUserInterceptor() {
        super(LoginUser.class, List.of(
            CatsnapAuthority.MODEL,
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ));
    }
}
