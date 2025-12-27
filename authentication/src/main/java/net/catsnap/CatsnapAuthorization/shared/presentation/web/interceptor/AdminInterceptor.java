package net.catsnap.CatsnapAuthorization.shared.presentation.web.interceptor;

import java.util.List;
import net.catsnap.shared.auth.Admin;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.springframework.stereotype.Component;

/**
 * 관리자 권한 검증 인터셉터입니다.
 * <p>
 * {@link Admin} 어노테이션이 붙은 컨트롤러 메서드에 대해 ADMIN 권한을 검증합니다. 게이트웨이에서 발급한 Passport 헤더(X-Passport)의 권한 정보를
 * 확인하여 접근을 제어합니다.
 * </p>
 *
 * <h3>허용 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#ADMIN} - 관리자</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class UserController {
 *
 *     @Admin // ADMIN 권한만 접근 가능
 *     @DeleteMapping("/users/{id}")
 *     public void deleteUser(@PathVariable Long id) {
 *         // 관리자만 사용자 삭제 가능
 *     }
 * }
 * }</pre>
 *
 * @see Admin
 * @see AbstractAuthInterceptor
 */
@Component
public class AdminInterceptor extends AbstractAuthInterceptor<Admin> {

    public AdminInterceptor(PassportHandler passportHandler) {
        super(Admin.class, List.of(CatsnapAuthority.ADMIN), passportHandler);
    }
}