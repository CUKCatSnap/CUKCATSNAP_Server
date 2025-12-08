package net.catsnap.domain.user.repository;

import java.util.Optional;
import net.catsnap.domain.user.entity.UserTinyInformation;
import org.springframework.data.repository.Repository;

public interface UserTinyInformationRepository extends Repository<UserTinyInformation, Long> {

    Optional<UserTinyInformation> findById(Long id);
}
