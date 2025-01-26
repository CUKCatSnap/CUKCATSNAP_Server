package net.catsnap.domain.user.fakeuser.entity;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.catsnap.domain.user.entity.User;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.GrantedAuthority;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FakeUser extends User {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(CatsnapAuthority.ANONYMOUS);
    }
}
