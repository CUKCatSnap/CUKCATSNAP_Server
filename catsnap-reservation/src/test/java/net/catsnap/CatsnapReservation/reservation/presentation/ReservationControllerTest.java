package net.catsnap.CatsnapReservation.reservation.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import net.catsnap.CatsnapReservation.reservation.application.ReservationService;
import net.catsnap.CatsnapReservation.reservation.application.dto.request.ReservationCreateRequest;
import net.catsnap.CatsnapReservation.reservation.application.dto.response.ReservationCreateResponse;
import net.catsnap.CatsnapReservation.shared.fixture.PassportTestHelper;
import net.catsnap.CatsnapReservation.shared.presentation.error.PresentationErrorCode;
import net.catsnap.CatsnapReservation.shared.presentation.success.PresentationSuccessCode;
import net.catsnap.CatsnapReservation.shared.presentation.web.config.PassportConfig;
import net.catsnap.CatsnapReservation.shared.presentation.web.resolver.UserIdArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationController.class)
@DisplayName("ReservationController 테스트")
@Import({PassportConfig.class, PassportTestHelper.class, UserIdArgumentResolver.class})
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassportTestHelper passportTestHelper;

    @MockitoBean
    private ReservationService reservationService;

    @Nested
    class 예약_생성 {

        @Test
        void 예약_생성에_성공한다() throws Exception {
            // given
            Long modelId = 1L;
            ReservationCreateRequest request = new ReservationCreateRequest(
                1L, LocalDate.of(2025, 6, 16), LocalTime.of(10, 0));

            ReservationCreateResponse response = new ReservationCreateResponse(
                "550e8400-e29b-41d4-a716-446655440000",
                100000L,
                LocalDateTime.of(2025, 6, 15, 9, 15)
            );

            when(reservationService.createReservation(eq(modelId), any(ReservationCreateRequest.class)))
                .thenReturn(response);

            // when & then
            mockMvc.perform(
                    passportTestHelper.withModel(post("/reservation"), modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(PresentationSuccessCode.CREATE.getCode()))
                .andExpect(jsonPath("$.data.reservationNumber").value("550e8400-e29b-41d4-a716-446655440000"))
                .andExpect(jsonPath("$.data.amount").value(100000L))
                .andExpect(jsonPath("$.data.holdExpiresAt").exists());
        }

        @Test
        void 프로그램ID_누락_시_예외가_발생한다() throws Exception {
            // given
            Long modelId = 1L;
            String invalidRequest = """
                {
                    "reservationDate": "2025-06-16",
                    "startTime": "10:00"
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withModel(post("/reservation"), modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 예약날짜_누락_시_예외가_발생한다() throws Exception {
            // given
            Long modelId = 1L;
            String invalidRequest = """
                {
                    "programId": 1,
                    "startTime": "10:00"
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withModel(post("/reservation"), modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 시작시간_누락_시_예외가_발생한다() throws Exception {
            // given
            Long modelId = 1L;
            String invalidRequest = """
                {
                    "programId": 1,
                    "reservationDate": "2025-06-16"
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withModel(post("/reservation"), modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 인증되지_않은_사용자는_접근할_수_없다() throws Exception {
            // given
            ReservationCreateRequest request = new ReservationCreateRequest(
                1L, LocalDate.of(2025, 6, 16), LocalTime.of(10, 0));

            // when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/reservation"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.FORBIDDEN.getCode()));
        }

        @Test
        void 작가_권한으로는_접근할_수_없다() throws Exception {
            // given
            Long photographerId = 1L;
            ReservationCreateRequest request = new ReservationCreateRequest(
                1L, LocalDate.of(2025, 6, 16), LocalTime.of(10, 0));

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.FORBIDDEN.getCode()));
        }
    }
}