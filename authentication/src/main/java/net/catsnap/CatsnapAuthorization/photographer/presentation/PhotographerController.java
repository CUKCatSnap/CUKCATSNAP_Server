package net.catsnap.CatsnapAuthorization.photographer.presentation;

import jakarta.validation.Valid;
import net.catsnap.CatsnapAuthorization.photographer.application.PhotographerService;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerSignUpRequest;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.CommonResultCode;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.ResultResponse;
import net.catsnap.shared.auth.AnyUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Photographer(작가) 관련 API 컨트롤러
 *
 * <p>회원가입, 인증 등 작가 관리 기능을 제공합니다.</p>
 */
@RestController
@RequestMapping("/authorization/photographer")
public class PhotographerController {

    private final PhotographerService photographerService;

    public PhotographerController(PhotographerService photographerService) {
        this.photographerService = photographerService;
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
        @Valid @RequestBody PhotographerSignUpRequest request) {
        photographerService.signUp(request);
        return ResultResponse.of(CommonResultCode.COMMON_CREATE);
    }
}