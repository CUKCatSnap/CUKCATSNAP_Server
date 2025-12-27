package net.catsnap.CatsnapAuthorization.session.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catsnap.CatsnapAuthorization.session.application.SessionService;
import net.catsnap.CatsnapAuthorization.session.application.dto.request.TokenRefreshRequest;
import net.catsnap.CatsnapAuthorization.session.application.dto.response.TokenRefreshResponse;
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import net.catsnap.CatsnapAuthorization.shared.fixture.PassportTestHelper;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.CommonResultCode;
import net.catsnap.CatsnapAuthorization.shared.presentation.web.config.PassportConfig;
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

@WebMvcTest(SessionController.class)
@DisplayName("SessionController 테스트")
@Import({PassportConfig.class, PassportTestHelper.class})
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassportTestHelper passportTestHelper;

    @MockitoBean
    private SessionService sessionService;

    @Nested
    class 토큰_갱신 {

        @Test
        void 토큰_갱신을_성공한다() throws Exception {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest(
                "550e8400-e29b-41d4-a716-446655440000"
            );
            TokenRefreshResponse response = new TokenRefreshResponse(
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U"
            );

            when(sessionService.refreshAccessToken(any(String.class))).thenReturn(response);

            // when & then
            mockMvc.perform(passportTestHelper.withAnonymous(post("/authorization/refresh"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(CommonResultCode.COMMON_READ.getCode()))
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()));
        }

        @Test
        void refreshToken_누락_시_예외가_발생한다() throws Exception {
            // given - refreshToken이 누락된 요청
            String invalidRequest = """
                {
                }
                """;

            // when & then
            mockMvc.perform(passportTestHelper.withAnonymous(post("/authorization/refresh"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 빈_refreshToken_전달_시_예외가_발생한다() throws Exception {
            // given - 빈 refreshToken 전달
            TokenRefreshRequest request = new TokenRefreshRequest("");

            // when & then
            mockMvc.perform(passportTestHelper.withAnonymous(post("/authorization/refresh"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void null_refreshToken_전달_시_예외가_발생한다() throws Exception {
            // given - null refreshToken 전달
            String invalidRequest = """
                {
                    "refreshToken": null
                }
                """;

            // when & then
            mockMvc.perform(passportTestHelper.withAnonymous(post("/authorization/refresh"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 유효하지_않은_refreshToken으로_예외가_발생한다() throws Exception {
            // given
            TokenRefreshRequest request = new TokenRefreshRequest("invalid-refresh-token");

            when(sessionService.refreshAccessToken(any(String.class)))
                .thenThrow(new BusinessException(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION,
                    "유효하지 않거나 만료된 리프레시 토큰입니다."));

            // when & then
            mockMvc.perform(passportTestHelper.withAnonymous(post("/authorization/refresh"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.DOMAIN_CONSTRAINT_VIOLATION.getCode()))
                .andExpect(jsonPath("$.message").value("해당 값이 유효하지 않습니다."));
        }
    }
}