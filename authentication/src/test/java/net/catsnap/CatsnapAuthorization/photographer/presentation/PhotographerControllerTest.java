package net.catsnap.CatsnapAuthorization.photographer.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catsnap.CatsnapAuthorization.photographer.application.PhotographerService;
import net.catsnap.CatsnapAuthorization.photographer.dto.request.PhotographerSignUpRequest;
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

@WebMvcTest(PhotographerController.class)
@DisplayName("PhotographerController 테스트")
@Import({PassportConfig.class, PassportTestHelper.class})
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PhotographerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PassportTestHelper passportTestHelper;

    @MockitoBean
    private PhotographerService photographerService;

    @Nested
    class 회원가입 {

        @Test
        void 회원가입을_성공한다() throws Exception {
            //given
            PhotographerSignUpRequest request = new PhotographerSignUpRequest(
                "newphotographer",
                "password1234",
                "김작가",
                "010-1234-5678"
            );
            doNothing().when(photographerService).signUp(any(PhotographerSignUpRequest.class));

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(CommonResultCode.COMMON_CREATE.getCode()));
        }

        @Test
        void 필수_값_누락_시_예외가_발생한다() throws Exception {
            //given - identifier가 누락된 요청
            String invalidRequest = """
                {
                    "password": "password1234",
                    "name": "김작가",
                    "phoneNumber": "010-1234-5678"
                }
                """;

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 빈_값_전달_시_예외가_발생한다() throws Exception {
            //given - 빈 identifier 전달
            PhotographerSignUpRequest request = new PhotographerSignUpRequest(
                "",
                "password1234",
                "김작가",
                "010-1234-5678"
            );

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void null_값_전달_시_예외가_발생한다() throws Exception {
            //given - null password 전달
            String invalidRequest = """
                {
                    "identifier": "newphotographer",
                    "password": null,
                    "name": "김작가",
                    "phoneNumber": "010-1234-5678"
                }
                """;

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void 빈_name_전달_시_예외가_발생한다() throws Exception {
            //given - 빈 name 전달
            PhotographerSignUpRequest request = new PhotographerSignUpRequest(
                "newphotographer",
                "password1234",
                "",
                "010-1234-5678"
            );

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }

        @Test
        void null_phoneNumber_전달_시_예외가_발생한다() throws Exception {
            //given - null phoneNumber 전달
            String invalidRequest = """
                {
                    "identifier": "newphotographer",
                    "password": "password1234",
                    "name": "김작가",
                    "phoneNumber": null
                }
                """;

            //when & then
            mockMvc.perform(
                    passportTestHelper.withAnonymous(post("/authorization/photographer/signup"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
        }
    }
}