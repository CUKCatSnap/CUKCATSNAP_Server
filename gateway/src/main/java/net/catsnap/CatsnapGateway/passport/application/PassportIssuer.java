package net.catsnap.CatsnapGateway.passport.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * Passport 발급 서비스. Gateway에서 인증된 사용자 정보를 서명된 Passport로 변환하여 다운스트림 서비스로 전달합니다.
 */
@Service
@RequiredArgsConstructor
public class PassportIssuer {

    private static final int EXPIRATION_MINUTES = 5;
    private static final byte PASSPORT_VERSION = 1;

    private final PassportHandler passportHandler;

    /**
     * 사용자 인증 정보를 기반으로 서명된 Passport를 발급하여 HTTP 헤더에 추가합니다. 기존 Passport 헤더를 먼저 무효화한 후 새로운 Passport를
     * 발급합니다.
     *
     * @param serverHttpRequest HTTP 요청
     * @param userId            사용자 ID
     * @param authority         사용자 권한
     * @return Passport 헤더가 추가된 새로운 ServerHttpRequest
     */
    public ServerHttpRequest issuePassport(ServerHttpRequest serverHttpRequest, Long userId,
        CatsnapAuthority authority) {
        // 기존 Passport 헤더 무효화 (사용자가 의도적으로 주입한 값 제거)
        ServerHttpRequest invalidatedRequest = invalidatePassport(serverHttpRequest);

        // 2. Passport 생성
        Instant now = Instant.now();
        Instant exp = now.plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        Passport passport = new Passport(PASSPORT_VERSION, userId, authority, now, exp);

        // 서명
        String signedPassport = passportHandler.sign(passport);

        // HTTP 헤더에 추가
        return invalidatedRequest.mutate()
            .header(PassportHandler.PASSPORT_KEY, signedPassport)
            .build();
    }

    /**
     * 주어진 ServerHttpRequest에서 Passport 헤더를 무효화합니다. 사용자가 의도적으로 주입한 값을 제거합니다.
     *
     * @param serverHttpRequest HTTP 요청
     * @return Passport 헤더가 제거된 새로운 ServerHttpRequest
     */
    public ServerHttpRequest invalidatePassport(ServerHttpRequest serverHttpRequest) {
        return serverHttpRequest.mutate()
            .headers(httpHeaders -> httpHeaders.remove(PassportHandler.PASSPORT_KEY))
            .build();
    }
}
