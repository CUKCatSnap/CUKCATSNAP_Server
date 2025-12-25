package net.catsnap.shared.passport.infrastructure;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;

/**
 * 바이트 기반 Passport 발급 및 파싱 구현체.
 *
 * <p>바이트 레이아웃 (총 58 bytes):
 * <pre>
 * [0]:     version (1 byte)
 * [1-8]:   userId (long, 8 bytes)
 * [9]:     authority (byte, 1 byte) - enum ordinal
 * [10-17]: exp (long, 8 bytes, epoch seconds)
 * [18-25]: iat (long, 8 bytes, epoch seconds)
 * [26-57]: signature (32 bytes, HMAC-SHA256)
 * </pre>
 */
public class BinaryPassportHandler implements PassportHandler {

    private static final int TOTAL_SIZE = 58;
    private static final int DATA_SIZE = 26; // signature 제외

    private final SecretKey secretKey;

    public BinaryPassportHandler(SecretKey secretKey) {
        if (secretKey == null) {
            throw new IllegalArgumentException("secretKey는 null일 수 없습니다.");
        }
        this.secretKey = secretKey;
    }

    @Override
    public String sign(Passport passport) {
        if (passport == null) {
            throw new IllegalArgumentException("passport는 null일 수 없습니다.");
        }

        ByteBuffer buffer = ByteBuffer.allocate(TOTAL_SIZE);

        // 데이터 작성
        buffer.put(passport.version());
        buffer.putLong(passport.userId());
        buffer.put(passport.authority().toByte());
        buffer.putLong(passport.exp().getEpochSecond());
        buffer.putLong(passport.iat().getEpochSecond());

        // 서명 생성 (처음 26바이트)
        byte[] data = Arrays.copyOfRange(buffer.array(), 0, DATA_SIZE);
        byte[] signature = generateSignature(data);
        buffer.put(signature);

        // Base64 인코딩
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    @Override
    public Optional<Passport> parse(String signedPassport) {
        if (signedPassport == null || signedPassport.isBlank()) {
            return Optional.empty();
        }

        try {
            // Base64 디코딩
            byte[] bytes = Base64.getDecoder().decode(signedPassport);

            // 길이 검증
            if (bytes.length != TOTAL_SIZE) {
                return Optional.empty();
            }

            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            // 데이터 읽기
            byte version = buffer.get();
            long userId = buffer.getLong();
            byte authorityByte = buffer.get();
            long expSeconds = buffer.getLong();
            long iatSeconds = buffer.getLong();

            byte[] receivedSignature = new byte[32];
            buffer.get(receivedSignature);

            // 서명 검증
            byte[] data = Arrays.copyOfRange(bytes, 0, DATA_SIZE);
            byte[] computedSignature = generateSignature(data);

            if (!MessageDigest.isEqual(receivedSignature, computedSignature)) {
                return Optional.empty();
            }

            // Authority 변환
            CatsnapAuthority authority = CatsnapAuthority.fromByte(authorityByte);

            // 시간 변환
            Instant iat = Instant.ofEpochSecond(iatSeconds);
            Instant exp = Instant.ofEpochSecond(expSeconds);

            // 만료 확인
            if (Instant.now().isAfter(exp)) {
                return Optional.empty();
            }

            // Passport 생성
            Passport passport = new Passport(version, userId, authority, iat, exp);
            return Optional.of(passport);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * HMAC-SHA256 서명을 생성합니다.
     */
    private byte[] generateSignature(byte[] data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(secretKey);
            return hmac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("HMAC 서명 생성 실패", e);
        }
    }
}