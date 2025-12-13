package net.catsnap.CatsnapAuthorization.model.application;

import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import net.catsnap.CatsnapAuthorization.model.infrastructure.ModelRepository;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Model 애그리거트의 Application Service
 *
 * <p>도메인 계층의 객체들을 오케스트레이션하여 비즈니스 유스케이스를 구현합니다.
 *
 * @see Model
 * @see ModelRepository
 * @see PasswordEncoder
 */
@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * ModelService 생성자
     *
     * @param modelRepository Model 엔티티의 영속성 관리를 위한 Repository
     * @param passwordEncoder 비밀번호 암호화 및 검증을 위한 인터페이스
     */
    public ModelService(ModelRepository modelRepository, PasswordEncoder passwordEncoder) {
        this.modelRepository = modelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 유스케이스
     * <p>
     * 새로운 사용자를 시스템에 등록합니다.
     *
     * @param request 회원가입 요청 정보를 담은 DTO
     * @throws net.catsnap.CatsnapAuthorization.shared.exception.BusinessException 값 객체 생성 시 유효성 검증
     *                                                                             실패 또는 식별자 중복
     * @see ModelSignUpRequest
     */
    @Transactional
    public void signUp(ModelSignUpRequest request) {
        // 식별자 중복 체크
        if (checkIdentifierExists(request.identifier())) {
            throw new IllegalArgumentException("이미 존재하는 식별자입니다: " + request.identifier());
        }

        // Aggregate Root가 VO를 직접 생성하도록 원시 타입 전달
        Model model = Model.signUp(
            request.identifier(),
            request.password(),
            request.nickname(),
            request.birthday(),
            request.phoneNumber(),
            passwordEncoder
        );

        modelRepository.save(model);
    }

    /**
     * 식별자 중복 확인 조회 서비스
     *
     * <p>주어진 식별자가 이미 시스템에 등록되어 있는지 확인합니다.
     * 회원가입 전 프론트엔드에서 실시간 중복 체크에 사용될 수 있습니다.</p>
     *
     * @param identifierValue 중복 확인할 식별자 문자열
     * @return 식별자가 이미 존재하면 {@code true}, 그렇지 않으면 {@code false}
     * @throws net.catsnap.CatsnapAuthorization.shared.exception.BusinessException 식별자 형식이 유효하지 않은
     *                                                                             경우 (값 객체 생성 실패)
     */
    @Transactional(readOnly = true)
    public boolean checkIdentifierExists(String identifierValue) {
        // 내부에서 VO로 변환하여 Repository 조회
        Identifier identifier = new Identifier(identifierValue);
        return modelRepository.existsByIdentifier(identifier);
    }
}
