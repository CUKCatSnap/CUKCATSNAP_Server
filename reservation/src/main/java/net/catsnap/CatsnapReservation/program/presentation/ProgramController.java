package net.catsnap.CatsnapReservation.program.presentation;

import jakarta.validation.Valid;
import net.catsnap.CatsnapReservation.program.application.ProgramService;
import net.catsnap.CatsnapReservation.program.application.dto.request.ProgramCreateRequest;
import net.catsnap.CatsnapReservation.program.application.dto.response.ProgramResponse;
import net.catsnap.CatsnapReservation.shared.presentation.response.ResultResponse;
import net.catsnap.CatsnapReservation.shared.presentation.success.PresentationSuccessCode;
import net.catsnap.CatsnapReservation.shared.presentation.web.resolver.UserId;
import net.catsnap.shared.auth.LoginPhotographer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 프로그램 관련 API 컨트롤러
 *
 * <p>작가의 프로그램 생성, 수정, 삭제, 조회 기능을 제공합니다.</p>
 *
 * <p>기본 경로: {@code /reservation/program}</p>
 */
@RestController
@RequestMapping("/reservation/program")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    /**
     * 프로그램 생성 API
     *
     * <p>요청 경로: {@code POST /reservation/program}</p>
     * <p>응답 타입: {@code ResponseEntity<ResultResponse<ProgramResponse>>}</p>
     *
     * @param photographerId 인증된 작가 ID
     * @param request        프로그램 생성 요청 정보
     * @return 생성 성공 응답(생성된 프로그램 ID 포함)
     */
    @PostMapping
    @LoginPhotographer
    public ResponseEntity<ResultResponse<ProgramResponse>> createProgram(
        @UserId Long photographerId,
        @Valid @RequestBody ProgramCreateRequest request
    ) {
        ProgramResponse response = programService.createProgram(photographerId, request);
        return ResultResponse.of(PresentationSuccessCode.CREATE, response);
    }
}
