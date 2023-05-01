package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.MemoryOpenMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryOpenMemberRepository extends JpaRepository<MemoryOpenMember, Integer> {
    boolean existsByCapsuleIdAndMemberId(int capsuleId, int memberId);
    boolean existsByMemoryIdAndMemberId(int memoryId, int memberId);
    int countByCapsuleIdAndMemberId(int capsuleId, int memberId);
}
