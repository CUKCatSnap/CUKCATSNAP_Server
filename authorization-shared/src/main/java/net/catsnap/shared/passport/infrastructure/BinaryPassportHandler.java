package net.catsnap.shared.passport.infrastructure;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import net.catsnap.shared.auth.CatsnapAuthority;
import net.catsnap.shared.passport.domain.Passport;
import net.catsnap.shared.passport.domain.PassportHandler;
import net.catsnap.shared.passport.domain.exception.ExpiredPassportException;
import net.catsnap.shared.passport.domain.exception.InvalidPassportException;
import net.catsnap.shared.passport.domain.exception.PassportParsingException;

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

    public BinaryPassportHandler(String secretKeyString) {
        if (secretKeyString == null || secretKeyString.isBlank()) {
            throw new IllegalArgumentException("secretKeyString은 null이거나 비어있을 수 없습니다.");
        }

        // UTF-8 인코딩으로 바이트 배열 생성
        byte[] keyBytes = secretKeyString.getBytes(StandardCharsets.UTF_8);

        // HMAC-SHA256 보안을 위해 최소 32바이트 키 길이 검증
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(
                "Secret key must be at least 32 bytes long for HMAC-SHA256 security. " +
                    "Current key length: " + keyBytes.length + " bytes"
            );
        }

        this.secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
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
        buffer.putLong(passport.iat().getEpochSecond());
        buffer.putLong(passport.exp().getEpochSecond());

        // 서명 생성 (처음 26바이트)
        byte[] data = Arrays.copyOfRange(buffer.array(), 0, DATA_SIZE);
        byte[] signature = generateSignature(data);
        buffer.put(signature);

        // Base64 인코딩
        return Base64.getEncoder().encodeToString(buffer.array());
    }

    @Override
    public Passport parse(String signedPassport) {
        if (signedPassport == null || signedPassport.isBlank()) {
            throw new PassportParsingException("Passport 문자열이 null이거나 비어있습니다");
        }

        try {
            // Base64 디코딩
            byte[] bytes;
            try {
                bytes = Base64.getDecoder().decode(signedPassport);
            } catch (IllegalArgumentException e) {
                throw new PassportParsingException("Base64 디코딩 실패", e);
            }

            // 길이 검증
            if (bytes.length != TOTAL_SIZE) {
                throw new PassportParsingException(
                    String.format("잘못된 Passport 길이: expected=%d, actual=%d", TOTAL_SIZE,
                        bytes.length));
            }

            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            // 데이터 읽기
            byte version = buffer.get();
            long userId = buffer.getLong();
            byte authorityByte = buffer.get();
            long iatSeconds = buffer.getLong();
            long expSeconds = buffer.getLong();

            byte[] receivedSignature = new byte[32];
            buffer.get(receivedSignature);

            // 서명 검증
            byte[] data = Arrays.copyOfRange(bytes, 0, DATA_SIZE);
            byte[] computedSignature = generateSignature(data);

            if (!MessageDigest.isEqual(receivedSignature, computedSignature)) {
                throw new InvalidPassportException("Passport 서명 검증 실패");
            }

            // Authority 변환
            CatsnapAuthority authority;
            try {
                authority = CatsnapAuthority.fromByte(authorityByte);
            } catch (IllegalArgumentException e) {
                throw new InvalidPassportException("유효하지 않은 권한 정보", e);
            }

            // 시간 변환
            Instant iat = Instant.ofEpochSecond(iatSeconds);
            Instant exp = Instant.ofEpochSecond(expSeconds);

            // Passport 생성
            return new Passport(version, userId, authority, iat, exp);

        } catch (PassportParsingException | InvalidPassportException | ExpiredPassportException e) {
            // Passport 예외는 그대로 재던지기
            throw e;
        } catch (Exception e) {
            // 예상하지 못한 예외는 PassportParsingException으로 래핑
            throw new PassportParsingException("Passport 파싱 중 예상치 못한 오류 발생", e);
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