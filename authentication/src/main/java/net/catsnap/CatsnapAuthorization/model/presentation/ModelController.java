package net.catsnap.CatsnapAuthorization.model.presentation;

import jakarta.validation.Valid;
import net.catsnap.CatsnapAuthorization.model.application.ModelService;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.CommonResultCode;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.ResultResponse;
import net.catsnap.shared.auth.AnyUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Model(사용자) 관련 API 컨트롤러
 *
 * <p>회원가입, 인증 등 사용자 관리 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/authorization/model")
public class ModelController {

    private final ModelService modelService;

    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    /**
     * 회원가입 API
     *
     * @param request 회원가입 요청 정보
     * @return 성공 응답
     */
    @PostMapping("/signup")
    @AnyUser
    public ResponseEntity<ResultResponse<Void>> signUp(
        @Valid @RequestBody ModelSignUpRequest request) {
        modelService.signUp(request);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE);
    }
}
