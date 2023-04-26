package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.CapsuleMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapsuleMemberRepository extends JpaRepository<CapsuleMember, Integer> {
}
