package net.catsnap.domain.user.member.repository;

import java.util.Optional;
import net.catsnap.domain.user.member.entity.Member;
import net.catsnap.domain.user.member.entity.SnsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByIdentifier(String identifier);

    Optional<Member> findBySnsIdAndSnstype(String snsId, SnsType snsType);
}
