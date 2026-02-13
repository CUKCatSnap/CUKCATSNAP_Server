package net.catsnap.domain.user.repository;

import java.util.Optional;
import net.catsnap.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdentifier(String identifier);
}
