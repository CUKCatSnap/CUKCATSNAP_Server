package net.catsnap.CatsnapAuthorization.model.infrastructure;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import net.catsnap.CatsnapAuthorization.model.domain.Model;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Identifier;
import net.catsnap.CatsnapAuthorization.model.domain.vo.Nickname;
import net.catsnap.CatsnapAuthorization.model.domain.vo.PhoneNumber;
import net.catsnap.CatsnapAuthorization.model.domain.vo.RawPassword;
import net.catsnap.CatsnapAuthorization.password.domain.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("ModelRepository 테스트")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ModelRepositoryTest {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Identifier testIdentifier;
    private RawPassword testRawPassword;
    private Nickname testNickname;
    private PhoneNumber testPhoneNumber;
    private LocalDate testBirthday;

    @BeforeEach
    void setUp() {
        testIdentifier = new Identifier("testuser");
        testRawPassword = new RawPassword("password1234");
        testNickname = new Nickname("테스터");
        testPhoneNumber = new PhoneNumber("010-1234-5678");
        testBirthday = LocalDate.of(1990, 1, 1);
    }

    @Test
    void 존재하는_Identifier는_true를_반환한다() {
        // given
        Model model = Model.signUp(testIdentifier, testRawPassword, testNickname,
            testBirthday, testPhoneNumber, passwordEncoder);
        modelRepository.save(model);

        // when
        boolean exists = modelRepository.existsByIdentifier(testIdentifier);

        // then
        assertTrue(exists);
    }

    @Test
    void 존재하지_않는_Identifier는_false를_반환한다() {
        // given
        Identifier nonExistentIdentifier = new Identifier("nonexistent");

        // when
        boolean exists = modelRepository.existsByIdentifier(nonExistentIdentifier);

        // then
        assertFalse(exists);
    }
}