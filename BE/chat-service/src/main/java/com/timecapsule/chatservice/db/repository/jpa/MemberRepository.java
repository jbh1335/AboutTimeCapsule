package com.timecapsule.chatservice.db.repository.jpa;

import com.timecapsule.chatservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(Integer memberId);
}
