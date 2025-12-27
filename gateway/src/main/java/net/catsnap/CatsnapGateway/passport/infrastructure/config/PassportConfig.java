package net.catsnap.CatsnapGateway.passport.infrastructure.config;

import net.catsnap.shared.passport.domain.PassportHandler;
import net.catsnap.shared.passport.infrastructure.BinaryPassportHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Passport 관련 인프라스트럭처 설정. authorization-shared의 PassportHandler 구현체를 Spring Bean으로 등록합니다.
 */
@Configuration
public class PassportConfig {

    @Value("${passport.secret-key}")
    private String secretKeyString;

    /**
     * PassportHandler 빈을 생성합니다. BinaryPassportHandler 구현체를 사용하여 바이트 기반 서명된 Passport를 발급하고 파싱합니다.
     *
     * @return PassportHandler 구현체
     */
    @Bean
    public PassportHandler passportHandler() {
        return new BinaryPassportHandler(secretKeyString);
    }
}
