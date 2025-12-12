package net.catsnap.CatsnapAuthorization.model.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.catsnap.CatsnapAuthorization.model.domain.vo.EncodedPassword;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Nickname;
import net.catsnap.CatsnapAuthorization.model.domain.vo.PhoneNumber;
import net.catsnap.CatsnapAuthorization.model.domain.vo.RawPassword;
import net.catsnap.CatsnapAuthorization.model.infrastructure.converter.EncodedPasswordConverter;
import net.catsnap.CatsnapAuthorization.model.infrastructure.converter.IdentifierConverter;
import net.catsnap.CatsnapAuthorization.model.infrastructure.converter.NicknameConverter;
import net.catsnap.CatsnapAuthorization.model.infrastructure.converter.PhoneNumberConverter;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 모델(사용자) 애그리거트 루트
 *
 * <p>시스템의 사용자를 표현하는 핵심 도메인 엔티티입니다.
 * DDD의 애그리거트 루트로서 사용자 관련 비즈니스 로직과 규칙을 캡슐화합니다.</p>
 *
 * @see Identifier
 * @see Nickname
 * @see RawPassword
 * @see EncodedPassword
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Model {

    private static final String DEFAULT_PROFILE_PHOTO_URL = "https://placehold.co/100x100";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Convert(converter = IdentifierConverter.class)
    private Identifier identifier;

    @Column(nullable = false)
    @Convert(converter = EncodedPasswordConverter.class)
    private EncodedPassword password;

    @Column(nullable = false)
    @Convert(converter = NicknameConverter.class)
    private Nickname nickname;

    private LocalDate birthday;

    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phoneNumber;

    private String profilePhotoUrl;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    /**
     * 회원가입을 위한 정적 팩토리 메서드
     *
     * <p>비즈니스 규칙을 검증하고 비밀번호를 암호화하여 Model 객체를 생성합니다.
     * 모든 값 객체는 생성 시점에 자동으로 유효성 검증이 수행됩니다.</p>
     *
     * @param identifier      고유 식별자 (값 객체로 자동 검증)
     * @param rawPassword     평문 비밀번호 (값 객체로 자동 검증)
     * @param nickname        닉네임 (값 객체로 자동 검증)
     * @param birthday        생년월일
     * @param phoneNumber     전화번호 (값 객체로 자동 검증)
     * @param passwordEncoder 비밀번호 암호화 인터페이스
     * @return 생성된 Model 엔티티
     * @see RawPassword
     * @see EncodedPassword
     */
    public static Model signUp(Identifier identifier, RawPassword rawPassword, Nickname nickname,
        LocalDate birthday, PhoneNumber phoneNumber, PasswordEncoder passwordEncoder) {
        // RawPassword 값 객체가 이미 검증을 수행했으므로 별도 검증 불필요
        EncodedPassword encodedPassword = rawPassword.encode(passwordEncoder);
        return new Model(identifier, encodedPassword, nickname, birthday, phoneNumber);
    }

    private Model(Identifier identifier, EncodedPassword encodedPassword, Nickname nickname,
        LocalDate birthday, PhoneNumber phoneNumber) {
        this.identifier = identifier;
        this.password = encodedPassword;
        this.nickname = nickname;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.profilePhotoUrl = DEFAULT_PROFILE_PHOTO_URL;
    }
}
