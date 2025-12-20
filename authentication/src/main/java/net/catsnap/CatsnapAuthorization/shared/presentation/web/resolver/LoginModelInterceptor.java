package net.catsnap.CatsnapAuthorization.shared.presentation.web.resolver;

import java.util.List;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.auth.LoginModel;
import org.springframework.stereotype.Component;

/**
 * 모델 권한 검증 인터셉터입니다.
 * <p>
 * {@link LoginModel} 어노테이션이 붙은 컨트롤러 메서드에 대해 모델 권한을 검증합니다. 모델(MODEL)과 관리자(ADMIN)만 접근할 수 있습니다.
 * </p>
 *
 * <h3>허용 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#MODEL} - 모델</li>
 *   <li>{@link CatsnapAuthority#ADMIN} - 관리자</li>
 * </ul>
 *
 * <h3>거부 권한</h3>
 * <ul>
 *   <li>{@link CatsnapAuthority#PHOTOGRAPHER} - 사진작가</li>
 *   <li>{@link CatsnapAuthority#ANONYMOUS} - 익명 사용자</li>
 * </ul>
 *
 * <h3>사용 예시</h3>
 * <pre>{@code
 * @RestController
 * public class BookingController {
 *
 *     @LoginModel // 모델과 관리자만 접근 가능
 *     @GetMapping("/bookings/my")
 *     public List<BookingResponse> getMyBookings() {
 *         // 모델이 자신의 촬영 예약 목록 조회
 *     }
 * }
 * }</pre>
 *
 * @see LoginModel
 * @see AbstractAuthInterceptor
 */
@Component
public class LoginModelInterceptor extends AbstractAuthInterceptor<LoginModel> {

    public LoginModelInterceptor() {
        super(LoginModel.class, List.of(
            CatsnapAuthority.MODEL,
            CatsnapAuthority.ADMIN
        ));
    }
}