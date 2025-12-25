package net.catsnap.shared.passport.domain;

import java.time.Instant;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;

/**
 * 검증된 사용자 인증 정보를 담는 Value Object. Gateway에서 발급되어 내부 서비스 간 전달됩니다.
 *
 * <p>이 객체는 이미 서명 검증이 완료된 상태의 인증 정보를 나타냅니다.
 * 서명 검증은 PassportParser에서 수행되며, 검증된 후에는 신뢰할 수 있는 정보로 사용됩니다.</p>
 */
public record Passport(
    byte version,
    long userId,
    CatsnapAuthority authority,
    Instant iat,
    Instant exp
) {

    /**
     * Compact constructor - 유효성 검증
     */
    public Passport {
        if (version < 1) {
            throw new IllegalArgumentException("version은 1 이상이어야 합니다.");
        }
        if (authority == null) {
            throw new IllegalArgumentException("authority는 null일 수 없습니다.");
        }
        if (iat == null || exp == null) {
            throw new IllegalArgumentException("iat와 exp는 null일 수 없습니다.");
        }
        if (iat.isAfter(exp)) {
            throw new IllegalArgumentException("iat는 exp보다 이전이어야 합니다.");
        }
    }

    /**
     * Passport가 만료되었는지 확인합니다.
     *
     * @return 현재 시간이 만료 시간을 지났으면 true, 아니면 false
     */
    public boolean isExpired() {
        return Instant.now().isAfter(exp);
    }

    @Override
    public long userId() {
        if (!isExpired()) {
            return userId;
        } else {
            throw new ExpiredPassportException();
        }
    }

    @Override
    public CatsnapAuthority authority() {
        if (!isExpired()) {
            return authority;
        } else {
            throw new ExpiredPassportException();
        }
    }
}
