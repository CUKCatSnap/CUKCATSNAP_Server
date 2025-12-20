package net.catsnap.shared.principal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.annotation.Annotation;
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
class UserIdTest {

    @Test
    void Target이_PARAMETER여야_한다() {
        // given
        Target target = UserId.class.getAnnotation(Target.class);

        // when & then
        assertNotNull(target);
        assertArrayEquals(new ElementType[]{ElementType.PARAMETER}, target.value());
    }

    @Test
    void Retention이_RUNTIME이어야_한다() {
        // given
        Retention retention = UserId.class.getAnnotation(Retention.class);

        // when & then
        assertNotNull(retention);
        assertEquals(RetentionPolicy.RUNTIME, retention.value());
    }

    @Test
    void 파라미터에_적용되고_리플렉션으로_읽을_수_있어야_한다() throws NoSuchMethodException {
        // given
        class TestClass {

            public void testMethod(@UserId Long userId) {
            }
        }

        // when
        Method method = TestClass.class.getMethod("testMethod", Long.class);
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        // then
        assertEquals(1, parameterAnnotations.length);
        assertEquals(1, parameterAnnotations[0].length);
        assertInstanceOf(UserId.class, parameterAnnotations[0][0]);
    }
}

