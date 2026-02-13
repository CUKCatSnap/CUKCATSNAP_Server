package net.catsnap.CatsnapAuthorization.session.presentation;

import jakarta.validation.Valid;
import net.catsnap.CatsnapAuthorization.session.application.SessionService;
import net.catsnap.CatsnapAuthorization.session.application.dto.request.TokenRefreshRequest;
import net.catsnap.CatsnapAuthorization.session.application.dto.response.TokenRefreshResponse;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.CommonResultCode;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.ResultResponse;
import net.catsnap.shared.auth.AnyUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Session 관련 API 컨트롤러
 *
 * <p>토큰 갱신 등 세션 관리 기능을 제공합니다.
 * Model과 Photographer가 공통으로 사용하는 엔드포인트입니다.</p>
 */
@RestController
@RequestMapping("/authorization")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * 토큰 갱신 API
     *
     * <p>리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급합니다.</p>
     *
     * @param request 리프레시 토큰을 담은 요청 정보
     * @return 새로 발급된 액세스 토큰
     */
    @PostMapping("/refresh")
    @AnyUser
    public ResponseEntity<ResultResponse<TokenRefreshResponse>> refreshToken(
        @Valid @RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse response = sessionService.refreshAccessToken(request.refreshToken());
        return ResultResponse.of(CommonResultCode.COMMON_READ, response);
    }
}