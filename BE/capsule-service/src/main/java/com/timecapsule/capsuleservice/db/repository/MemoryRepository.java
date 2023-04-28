package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.Capsule;
import com.timecapsule.capsuleservice.db.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Integer> {
}
