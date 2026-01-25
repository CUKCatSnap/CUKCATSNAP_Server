package net.catsnap.CatsnapReservation.program.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catsnap.CatsnapReservation.program.application.ProgramService;
import net.catsnap.CatsnapReservation.program.application.dto.request.ProgramCreateRequest;
import net.catsnap.CatsnapReservation.program.application.dto.response.ProgramResponse;
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

@WebMvcTest(ProgramController.class)
@DisplayName("ProgramController 테스트")
@Import({PassportConfig.class, PassportTestHelper.class, UserIdArgumentResolver.class})
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProgramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassportTestHelper passportTestHelper;

    @MockitoBean
    private ProgramService programService;

    @Nested
    class 프로그램_생성 {

        @Test
        void 프로그램_생성에_성공한다() throws Exception {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅 촬영",
                "아름다운 웨딩 스냅을 촬영해드립니다.",
                150000L,
                90
            );

            ProgramResponse response = new ProgramResponse(1L);

            when(programService.createProgram(eq(photographerId), any(ProgramCreateRequest.class)))
                .thenReturn(response);

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(PresentationSuccessCode.CREATE.getCode()))
                .andExpect(jsonPath("$.data.id").value(1L));
        }

        @Test
        void 설명_없이_프로그램_생성에_성공한다() throws Exception {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "프로필 촬영",
                null,
                100000L,
                60
            );

            ProgramResponse response = new ProgramResponse(1L);

            when(programService.createProgram(eq(photographerId), any(ProgramCreateRequest.class)))
                .thenReturn(response);

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1L));
        }

        @Test
        void 제목_누락_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            String invalidRequest = """
                {
                    "description": "설명입니다.",
                    "price": 100000,
                    "durationMinutes": 60
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 빈_제목_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "",
                "설명입니다.",
                100000L,
                60
            );

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 가격_누락_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            String invalidRequest = """
                {
                    "title": "웨딩 스냅",
                    "description": "설명입니다.",
                    "durationMinutes": 60
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 음수_가격_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅",
                "설명입니다.",
                -1L,
                60
            );

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 소요시간_누락_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            String invalidRequest = """
                {
                    "title": "웨딩 스냅",
                    "description": "설명입니다.",
                    "price": 100000
                }
                """;

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 영분_소요시간_시_예외가_발생한다() throws Exception {
            // given
            Long photographerId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅",
                "설명입니다.",
                100000L,
                0
            );

            // when & then
            mockMvc.perform(
                    passportTestHelper.withPhotographer(post("/reservation/program"), photographerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 인증되지_않은_사용자는_접근할_수_없다() throws Exception {
            // given
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅",
                "설명입니다.",
                100000L,
                60
            );

            // when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/reservation/program"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.FORBIDDEN.getCode()));
        }

        @Test
        void 모델_권한으로는_접근할_수_없다() throws Exception {
            // given
            Long modelId = 1L;
            ProgramCreateRequest request = new ProgramCreateRequest(
                "웨딩 스냅",
                "설명입니다.",
                100000L,
                60
            );

            // when & then
            mockMvc.perform(
                    passportTestHelper.withModel(post("/reservation/program"), modelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(PresentationErrorCode.FORBIDDEN.getCode()));
        }
    }
}
