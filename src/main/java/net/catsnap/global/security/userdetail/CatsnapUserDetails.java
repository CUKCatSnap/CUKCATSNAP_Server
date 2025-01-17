package net.catsnap.global.security.userdetail;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import net.catsnap.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CatsnapUserDetails implements UserDetails {

    private final User user;

    @Override
    public String getUsername() {
        return user.getIdentifier();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    public Long getDatabaseId() {
        return user.getId();
    }
}
