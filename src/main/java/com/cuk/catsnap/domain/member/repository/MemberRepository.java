package com.cuk.catsnap.domain.member.repository;

import com.cuk.catsnap.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByIdentifier(String identifier);
}
