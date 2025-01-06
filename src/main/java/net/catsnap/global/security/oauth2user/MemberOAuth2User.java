package net.catsnap.global.security.oauth2user;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.dto.OAuth2MemberResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@RequiredArgsConstructor
public class MemberOAuth2User implements OAuth2User {

    private final Long memberId;
    private final OAuth2MemberResponse oAuth2MemberResponse;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(CatsnapAuthority.MEMBER);
    }

    @Override
    public String getName() {
        return oAuth2MemberResponse.snsType().name() + "_" + oAuth2MemberResponse.snsId();
    }
}
