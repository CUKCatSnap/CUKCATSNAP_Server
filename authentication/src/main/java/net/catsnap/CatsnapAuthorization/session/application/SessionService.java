package net.catsnap.CatsnapAuthorization.session.application;

import net.catsnap.CatsnapAuthorization.session.application.dto.response.TokenRefreshResponse;
import net.catsnap.CatsnapAuthorization.session.domain.AccessTokenManager;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSession;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSessionRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Session 애그리거트의 Application Service
 *
 * <p>토큰 갱신 등 세션 관련 유스케이스를 구현합니다.
 * Model과 Photographer가 공통으로 사용하는 세션 관리 로직을 제공합니다.</p>
 *
 * @see LoginSession
 * @see LoginSessionRepository
 */
@Service
public class SessionService {

    private final LoginSessionRepository loginSessionRepository;
    private final AccessTokenManager accessTokenManager;

    /**
     * SessionService 생성자
     *
     * @param loginSessionRepository 로그인 세션 Repository
     * @param accessTokenManager     액세스 토큰 발급을 위한 인터페이스
     */
    public SessionService(LoginSessionRepository loginSessionRepository,
        AccessTokenManager accessTokenManager) {
        this.loginSessionRepository = loginSessionRepository;
        this.accessTokenManager = accessTokenManager;
    }

    /**
     * 리프레시 토큰으로 액세스 토큰 갱신 유스케이스
     *
     * <p>리프레시 토큰(sessionKey)을 검증하고 새로운 액세스 토큰을 발급합니다.
     * 세션의 마지막 접근 시간이 갱신되어 세션 만료가 연장됩니다.</p>
     *
     * @param refreshToken 리프레시 토큰 (sessionKey)
     * @return 새로 발급된 액세스 토큰
     * @throws BusinessException 리프레시 토큰이 유효하지 않거나 만료된 경우
     */
    @Transactional
    public TokenRefreshResponse refreshAccessToken(String refreshToken) {
        // 리프레시 토큰(sessionKey)으로 세션 조회
        LoginSession loginSession = loginSessionRepository.findById(refreshToken)
            .orElseThrow(() -> new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "유효하지 않거나 만료된 리프레시 토큰입니다."));

        // 액세스 토큰 재발급 (lastAccessedAt 자동 갱신)
        String newAccessToken = loginSession.generateAccessToken(accessTokenManager);

        // 3. 세션 저장 (lastAccessedAt 갱신 반영)
        loginSessionRepository.save(loginSession);

        return new TokenRefreshResponse(newAccessToken);
    }
}