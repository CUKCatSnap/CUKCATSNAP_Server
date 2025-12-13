package net.catsnap.CatsnapAuthorization.model.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.model.application.ModelService;
import net.catsnap.CatsnapAuthorization.model.dto.request.ModelSignUpRequest;
import net.catsnap.CatsnapAuthorization.shared.presentation.response.CommonResultCode;
import net.catsnap.CatsnapAuthorization.shared.domain.error.CommonErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ModelController.class)
@DisplayName("ModelController 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ModelService modelService;

    @Test
    void 회원가입을_성공한다() throws Exception {
        //given
        ModelSignUpRequest request = new ModelSignUpRequest(
            "newuser",
            "password1234",
            "새유저",
            LocalDate.of(1995, 5, 15),
            "010-1234-5678"
        );
        doNothing().when(modelService).signUp(any(ModelSignUpRequest.class));

        //when & then
        mockMvc.perform(post("/authorization/model/signup")
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
                "nickname": "새유저",
                "birthday": "1995-05-15",
                "phoneNumber": "010-1234-5678"
            }
            """;

        //when & then
        mockMvc.perform(post("/authorization/model/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
    }

    @Test
    void 빈_값_전달_시_예외가_발생한다() throws Exception {
        //given - 빈 identifier 전달
        ModelSignUpRequest request = new ModelSignUpRequest(
            "",
            "password1234",
            "새유저",
            LocalDate.of(1995, 5, 15),
            "010-1234-5678"
        );

        //when & then
        mockMvc.perform(post("/authorization/model/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
    }

    @Test
    void null_값_전달_시_예외가_발생한다() throws Exception {
        //given - null password 전달
        String invalidRequest = """
            {
                "identifier": "newuser",
                "password": null,
                "nickname": "새유저",
                "birthday": "1995-05-15",
                "phoneNumber": "010-1234-5678"
            }
            """;

        //when & then
        mockMvc.perform(post("/authorization/model/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(CommonErrorCode.INVALID_REQUEST_BODY.getCode()));
    }
}