package net.catsnap.domain.social.repository;

import java.util.Optional;
import net.catsnap.domain.social.entity.PhotographerSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerSubscribeRepository extends
    JpaRepository<PhotographerSubscribe, Integer> {

    Optional<PhotographerSubscribe> findByPhotographerIdAndMemberId(Long photographerId,
        Long memberId);
}
