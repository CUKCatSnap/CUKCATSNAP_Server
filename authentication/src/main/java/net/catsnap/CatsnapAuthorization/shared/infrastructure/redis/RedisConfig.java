package net.catsnap.CatsnapAuthorization.shared.infrastructure.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * Redis 설정 클래스
 */
@Configuration
@EnableRedisRepositories(basePackages = "net.catsnap.CatsnapAuthorization")
public class RedisConfig {

}