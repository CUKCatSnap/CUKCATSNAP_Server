package net.catsnap.domain.user.fakeuser.entity;

import jakarta.persistence.DiscriminatorValue;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import net.catsnap.domain.user.entity.User;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.GrantedAuthority;

@Getter
@SuperBuilder
@AllArgsConstructor
@DiscriminatorValue("FAKE_USER")
public class FakeUser extends User {

    public static Long fakeUserId = -1L;
    public static String fakeUserIdentifier = "anonymous";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(CatsnapAuthority.ANONYMOUS);
    }
}
