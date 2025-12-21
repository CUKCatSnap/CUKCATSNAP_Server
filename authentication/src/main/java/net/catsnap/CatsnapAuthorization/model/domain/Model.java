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
import net.catsnap.CatsnapAuthorization.shared.domain.BusinessException;
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
     * 값 객체는 내부에서 생성되며, 생성 시점에 자동으로 유효성 검증이 수행됩니다.</p>
     *
     * @param identifierValue  고유 식별자 문자열
     * @param rawPasswordValue 평문 비밀번호 문자열
     * @param nicknameValue    닉네임 문자열
     * @param birthday         생년월일
     * @param phoneNumberValue 전화번호 문자열
     * @param passwordEncoder  비밀번호 암호화 인터페이스
     * @return 생성된 Model 엔티티
     * @throws BusinessException 값 객체 생성 시 유효성 검증 실패
     * @see RawPassword
     * @see EncodedPassword
     */
    public static Model signUp(String identifierValue, String rawPasswordValue,
        String nicknameValue,
        LocalDate birthday, String phoneNumberValue, PasswordEncoder passwordEncoder) {
        Identifier identifier = new Identifier(identifierValue);
        RawPassword rawPassword = new RawPassword(rawPasswordValue);
        Nickname nickname = new Nickname(nicknameValue);
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberValue);

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

    /**
     * 로그인 시 비밀번호를 검증합니다.
     *
     * @param rawPasswordValue 사용자가 입력한 평문 비밀번호
     * @param passwordEncoder  비밀번호 검증을 위한 인터페이스
     * @return 비밀번호가 일치하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean validatePassword(String rawPasswordValue, PasswordEncoder passwordEncoder) {
        RawPassword rawPassword = new RawPassword(rawPasswordValue);
        return this.password.matches(rawPassword, passwordEncoder);
    }
}
