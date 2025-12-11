package net.catsnap.CatsnapCommon.authority;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CatsnapAuthorityTest {

    @Test
    void toString_메서드는_권한_이름의_소문자_버전을_반환해야_합니다() {
        for (CatsnapAuthority authority : CatsnapAuthority.values()) {
            assertEquals(authority.name().toLowerCase(), authority.toString());
        }
    }

}
