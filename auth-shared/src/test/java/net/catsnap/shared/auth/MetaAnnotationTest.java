package net.catsnap.shared.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MetaAnnotationTest {

    @Test
    void Authentication_어노테이션이_붙은_모든_권한_어노테이션을_찾을_수_있어야_한다() {
        // given
        Class<?>[] authAnnotations = new Class<?>[]{
            LoginUser.class,
            AnyUser.class,
            LoginPhotographer.class,
            LonginModel.class,
            Admin.class
        };

        // when & then
        for (Class<?> authAnnotation : authAnnotations) {
            Authentication authentication = authAnnotation.getAnnotation(Authentication.class);
            assertNotNull(authentication,
                authAnnotation.getSimpleName() + "에 @Authentication이 붙어있어야 합니다");
        }
    }
}

