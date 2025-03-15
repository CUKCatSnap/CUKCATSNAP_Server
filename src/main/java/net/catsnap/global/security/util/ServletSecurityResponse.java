package net.catsnap.global.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.security.dto.AccessTokenResponse;

@RequiredArgsConstructor
public class ServletSecurityResponse {

    private final ObjectMapper objectMapper;

    /*
     * 응답 메시지를 전송하는 메서드. resultCode를 받아서 ResultResponse로 변환하여 바디에 담아서 전송한다.
     */
    public void responseBody(HttpServletResponse response, ResultCode resultCode)
        throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(
            ResultResponse.ofNotEntity(resultCode));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }

    public void responseBody(HttpServletResponse response, ResultCode resultCode,
        AccessTokenResponse accessTokenResponse)
        throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(
            ResultResponse.of(resultCode, accessTokenResponse));
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}
