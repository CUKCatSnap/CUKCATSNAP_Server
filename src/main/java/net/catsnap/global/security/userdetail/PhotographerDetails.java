package net.catsnap.global.security.userdetail;

import net.catsnap.domain.user.photographer.entity.Photographer;
import net.catsnap.global.security.authority.CatsnapAuthority;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class PhotographerDetails implements UserDetails {

    private final Photographer photographer;

    @Override
    public String getUsername() {
        return photographer.getIdentifier();
    }

    @Override
    public String getPassword() {
        return photographer.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(CatsnapAuthority.PHOTOGRAPHER);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
