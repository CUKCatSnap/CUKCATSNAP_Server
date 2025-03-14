package net.catsnap.global.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.result.ResultCode;
import net.catsnap.global.result.ResultResponse;
import net.catsnap.global.security.dto.AccessTokenResponse;

@RequiredArgsConstructor
public class ServletSecurityResponse {

    private final ObjectMapper objectMapper;
    private final SecretKey key;

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

    /*
     * JWT 토큰 생성 메서드.
     *  expiration은 ms 단위임.
     * principal은 로그인 시 사용하는 id값이고  id는 데이터베이스에서 사용되는 유저의 id값임.
     */
    public String setJwtToken(Object provider, Object type, Object principal, Object authorities,
        Object id, Long expiration) {
        return Jwts.builder()
            .setHeader(Map.of(
                "provider", provider,
                "type", type
            ))
            .setClaims(Map.of(
                "identifier", principal,
                "authorities", authorities,
                "id", id
            ))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact();
    }
}
