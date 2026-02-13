package net.catsnap.CatsnapReservation.reservation.presentation;

import jakarta.validation.Valid;
import net.catsnap.CatsnapReservation.reservation.application.ReservationService;
import net.catsnap.CatsnapReservation.reservation.application.dto.request.ReservationCreateRequest;
import net.catsnap.CatsnapReservation.reservation.application.dto.response.ReservationCreateResponse;
import net.catsnap.CatsnapReservation.shared.presentation.response.ResultResponse;
import net.catsnap.CatsnapReservation.shared.presentation.success.PresentationSuccessCode;
import net.catsnap.CatsnapReservation.shared.presentation.web.resolver.UserId;
import net.catsnap.shared.auth.LoginModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예약 관련 API 컨트롤러
 *
 * <p>기본 경로: {@code /reservation}</p>
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /**
     * 예약 생성 API
     *
     * <p>요청 경로: {@code POST /reservation}</p>
     *
     * @param modelId 인증된 모델(고객) ID (passport에서 자동으로 추출됩니다)
     * @param request 예약 생성 요청 정보
     * @return 생성 성공 응답 (예약번호, 결제금액, 홀드 만료시각)
     */
    @PostMapping
    @LoginModel
    public ResponseEntity<ResultResponse<ReservationCreateResponse>> createReservation(
        @UserId Long modelId,
        @Valid @RequestBody ReservationCreateRequest request
    ) {
        ReservationCreateResponse response = reservationService.createReservation(modelId, request);
        return ResultResponse.of(PresentationSuccessCode.CREATE, response);
    }
}