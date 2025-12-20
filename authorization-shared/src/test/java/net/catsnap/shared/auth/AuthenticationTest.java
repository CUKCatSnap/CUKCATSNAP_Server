package net.catsnap.shared.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationTest {

    // 테스트 전용 어노테이션
    @Authentication
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface LocalAuth {

    }

    @Test
    void Target이_ANNOTATION_TYPE여야_한다() {
        // given
        Target target = Authentication.class.getAnnotation(Target.class);

        // when & then
        assertNotNull(target);
        assertEquals(ElementType.ANNOTATION_TYPE, target.value()[0]);
    }

    @Test
    void Retention이_RUNTIME이어야_한다() {
        // given
        Retention retention = Authentication.class.getAnnotation(Retention.class);

        // when & then
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void 다른_어노테이션에_붙일_수_있어야_한다() {
        // when
        Authentication ann = LocalAuth.class.getAnnotation(Authentication.class);

        // then
        assertNotNull(ann);
    }
}
