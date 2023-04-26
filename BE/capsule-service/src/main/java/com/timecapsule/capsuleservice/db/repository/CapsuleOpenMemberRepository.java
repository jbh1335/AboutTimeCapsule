package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.CapsuleOpenMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapsuleOpenMemberRepository extends JpaRepository<CapsuleOpenMember, Integer> {
    boolean existsByCapsuleIdAndMemberId(int capsuleId, int memberId);
}
