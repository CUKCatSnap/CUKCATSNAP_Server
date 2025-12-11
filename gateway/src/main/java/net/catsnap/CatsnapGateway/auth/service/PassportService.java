package net.catsnap.CatsnapGateway.auth.service;

import net.catsnap.CatsnapGateway.auth.dto.AuthenticationPassport;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

/**
 * PassportService는 사용자 인증 정보를 기반으로 HTTP 요청에 '여권' 헤더를 발행하거나 무효화하는 서비스를 제공합니다. 이 서비스는 게이트웨이에서 사용자 인증
 * 정보를 다운스트림 서비스로 전달하는 데 사용됩니다.
 */
@Service
public class PassportService {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String AUTHORITY_HEADER = "X-Authority";

    /**
     * 주어진 ServerHttpRequest에 사용자 인증 정보를 기반으로 '여권' 헤더를 발행합니다. 이 메서드는 사용자 ID와 권한 정보를 HTTP 헤더로 추가하여
     * 다운스트림 서비스에서 사용할 수 있도록 합니다.
     *
     * @param serverHttpRequest      헤더를 추가할 객체.
     * @param authenticationPassport 발행할 사용자 인증 정보를 담은 객체.
     * @return 여권 헤더가 추가된 새로운 ServerHttpRequest 객체.
     */
    public ServerHttpRequest issuePassport(ServerHttpRequest serverHttpRequest,
        AuthenticationPassport authenticationPassport) {
        return serverHttpRequest.mutate()
            .header(USER_ID_HEADER, String.valueOf(authenticationPassport.userId()))
            .header(AUTHORITY_HEADER, authenticationPassport.authority().getAuthorityName())
            .build();
    }

    /**
     * 주어진 ServerHttpRequest에서 '여권' 헤더를 무효화합니다. 이 메서드는 사용자 ID와 권한 헤더를 null로 설정하여 기존의 인증 정보를 제거합니다.
     * 이는 주로 로그아웃 또는 인증 정보가 더 이상 유효하지 않을 때 사용됩니다.
     * <p>
     * 또한, 사용자가 의도적으로 주입한 값을 무효화 합니다.
     *
     * @param serverHttpRequest 헤더를 무효화할 ServerHttpRequest 객체.
     * @return 여권 헤더가 무효화된 새로운 ServerHttpRequest 객체.
     */
    public ServerHttpRequest invalidatePassport(ServerHttpRequest serverHttpRequest) {
        return serverHttpRequest.mutate()
            .headers(httpHeaders -> {
                httpHeaders.remove(USER_ID_HEADER);
                httpHeaders.remove(AUTHORITY_HEADER);
            })
            .build();
    }

}
