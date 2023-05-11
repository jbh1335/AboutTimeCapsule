package com.timecapsule.memberservice.db.repository;

import com.timecapsule.memberservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findById(int id);
    List<Member> findByNicknameContaining(String nickname);
    boolean existsByNickname(String nickname);
}
