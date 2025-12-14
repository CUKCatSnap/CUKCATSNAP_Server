package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginPhotographer;
import org.springframework.stereotype.Component;

/**
 * 사진작가 권한 검증 인터셉터입니다.
 * <p>
 * {@link LoginPhotographer} 어노테이션이 붙은 컨트롤러 메서드에 대해 사진작가 권한을 검증합니다. 사진작가(PHOTOGRAPHER)와 관리자(ADMIN)만
 * 접근할 수 있습니다.
 * </p>
 *
 * <h3>허용 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#PHOTOGRAPHER} - 사진작가</li>
 *   <li>{@link CatsnapAuthority#ADMIN} - 관리자</li>
 * </ul>
 *
 * <h3>거부 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#MODEL} - 모델</li>
 *   <li>{@link CatsnapAuthority#ANONYMOUS} - 익명 사용자</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class PortfolioController {
 *
 *     @LoginPhotographer // 사진작가와 관리자만 접근 가능
 *     @PostMapping("/portfolios")
 *     public PortfolioResponse createPortfolio(@RequestBody PortfolioRequest request) {
 *         // 사진작가가 포트폴리오 생성
 *     }
 * }
 * }</pre>
 *
 * @see LoginPhotographer
 * @see AbstractAuthInterceptor
 */
@Component
public class LoginPhotographerInterceptor
    extends AbstractAuthInterceptor<LoginPhotographer> {

    public LoginPhotographerInterceptor() {
        super(LoginPhotographer.class, List.of(
            CatsnapAuthority.PHOTOGRAPHER,
            CatsnapAuthority.ADMIN
        ));
    }
}
