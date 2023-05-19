package com.timecapsule.oauthservice.db.repository;

import com.timecapsule.oauthservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(int id);
    Optional<Member> findByProviderId(String id);
    boolean existsByNickname(String nickname);
}
