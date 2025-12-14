package net.catsnap.shared.auth;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LoginPhotographerTest {

    @Test
    void Target이_METHOD여야_한다() {
        // given
        Target target = LoginPhotographer.class.getAnnotation(Target.class);

        // when & then
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.METHOD}, target.value());
    }

    @Test
    void Retention이_RUNTIME이어야_한다() {
        // given
        Retention retention = LoginPhotographer.class.getAnnotation(Retention.class);

        // when & then
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void Authentication_메타_어노테이션이_붙어있어야_한다() {
        // given
        Authentication authentication = LoginPhotographer.class.getAnnotation(Authentication.class);

        // when & then
        assertNotNull(authentication);
    }

    @Test
    void 메서드에_적용되고_리플렉션으로_읽을_수_있어야_한다() throws NoSuchMethodException {
        // given
        class TestClass {

            @LoginPhotographer
            public void testMethod() {
            }
        }

        // when
        Method method = TestClass.class.getMethod("testMethod");
        LoginPhotographer annotation = method.getAnnotation(LoginPhotographer.class);

        // then
        assertNotNull(annotation);
    }
}

