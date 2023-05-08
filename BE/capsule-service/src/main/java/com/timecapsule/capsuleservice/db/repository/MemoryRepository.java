package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Integer> {
    Optional<Memory> findById(int id);
    List<Memory> findAllByCapsuleIdAndIsDeletedFalse(int capsuleId);
    List<Memory> findAllByIsDeletedFalseAndIsLockedTrue();
}
