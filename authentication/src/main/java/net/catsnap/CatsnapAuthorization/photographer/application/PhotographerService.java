package net.catsnap.CatsnapAuthorization.photographer.application;

import java.time.ZoneId;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.dto.response.TokenResponse;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import net.catsnap.CatsnapAuthorization.photographer.domain.Photographer;
import net.catsnap.CatsnapAuthorization.photographer.domain.events.PhotographerCreatedEvent;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerLoginRequest;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerSignUpRequest;
import net.catsnap.CatsnapAuthorization.photographer.infrastructure.PhotographerRepository;
import net.catsnap.CatsnapAuthorization.session.domain.AccessTokenManager;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSession;
import net.catsnap.CatsnapAuthorization.session.domain.LoginSessionRepository;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import net.catsnap.shared.auth.CatsnapAuthority;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Photographer 애그리거트의 Application Service
 *
 * <p>도메인 계층의 객체들을 오케스트레이션하여 비즈니스 유스케이스를 구현합니다.
 *
 * @see Photographer
 * @see PhotographerRepository
 * @see PasswordEncoder
 */
@Service
public class PhotographerService {

    private final PhotographerRepository photographerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenManager accessTokenManager;
    private final LoginSessionRepository loginSessionRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * PhotographerService 생성자
     *
     * @param photographerRepository Photographer 엔티티의 영속성 관리를 위한 Repository
     * @param passwordEncoder        비밀번호 암호화 및 검증을 위한 인터페이스
     * @param accessTokenManager     액세스 토큰 발급을 위한 인터페이스
     * @param loginSessionRepository 로그인 세션 Repository
     * @param eventPublisher         Spring 이벤트 발행을 위한 인터페이스
     */
    public PhotographerService(PhotographerRepository photographerRepository,
        PasswordEncoder passwordEncoder, AccessTokenManager accessTokenManager,
        LoginSessionRepository loginSessionRepository, ApplicationEventPublisher eventPublisher) {
        this.photographerRepository = photographerRepository;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenManager = accessTokenManager;
        this.loginSessionRepository = loginSessionRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * 회원가입 유스케이스
     * <p>
     * 새로운 작가를 시스템에 등록합니다.
     *
     * @param request 회원가입 요청 정보를 담은 DTO
     * @throws BusinessException 값 객체 생성 시 유효성 검증 실패 또는 식별자 중복
     * @see PhotographerSignUpRequest
     */
    @Transactional
    public void signUp(PhotographerSignUpRequest request) {
        // 식별자 중복 체크
        if (checkIdentifierExists(request.identifier())) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "중복되는 identifier는 사용할 수 없습니다.");
        }

        // Aggregate Root가 VO를 직접 생성하도록 원시 타입 전달
        Photographer photographer = Photographer.signUp(
            request.identifier(),
            request.password(),
            request.name(),
            request.phoneNumber(),
            passwordEncoder
        );

        photographerRepository.save(photographer);

        // 도메인 이벤트 발행
        eventPublisher.publishEvent(
            new PhotographerCreatedEvent(
                photographer.getId(),
                photographer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()
            )
        );
    }

    /**
     * 로그인 유스케이스
     *
     * <p>사용자 인증 후 액세스 토큰과 로그인 세션을 생성합니다.
     *
     * @param request 로그인 요청 정보를 담은 DTO
     * @return 발급된 액세스 토큰과 로그인 세션 키
     * @throws BusinessException 식별자가 존재하지 않거나 비밀번호가 일치하지 않는 경우
     * @see PhotographerLoginRequest
     */
    @Transactional
    public TokenResponse login(PhotographerLoginRequest request) {
        // 식별자로 작가 조회
        Identifier identifier = new Identifier(request.identifier());
        Photographer photographer = photographerRepository.findByIdentifier(identifier)
            .orElseThrow(() -> new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "아이디 또는 비밀번호가 존재하지 않는 사용자입니다."));

        // 비밀번호 검증
        if (!photographer.validatePassword(request.password(), passwordEncoder)) {
            throw new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                "아이디 또는 비밀번호가 존재하지 않는 사용자입니다.");
        }

        // 로그인 세션 생성
        LoginSession loginSession = LoginSession.create(
            photographer.getId(),
            CatsnapAuthority.PHOTOGRAPHER
        );

        // 액세스 토큰 생성
        String accessToken = loginSession.generateAccessToken(accessTokenManager);

        loginSessionRepository.save(loginSession);

        return new TokenResponse(accessToken, loginSession.getSessionKey());
    }

    /**
     * 식별자 중복 확인 조회 서비스
     *
     * <p>주어진 식별자가 이미 시스템에 등록되어 있는지 확인합니다.
     * 회원가입 전 프론트엔드에서 실시간 중복 체크에 사용될 수 있습니다.</p>
     *
     * @param identifierValue 중복 확인할 식별자 문자열
     * @return 식별자가 이미 존재하면 {@code true}, 그렇지 않으면 {@code false}
     * @throws BusinessException 식별자 형식이 유효하지 않은 경우 (값 객체 생성 실패)
     */
    @Transactional(readOnly = true)
    public boolean checkIdentifierExists(String identifierValue) {
        // 내부에서 VO로 변환하여 Repository 조회
        Identifier identifier = new Identifier(identifierValue);
        return photographerRepository.existsByIdentifier(identifier);
    }
}