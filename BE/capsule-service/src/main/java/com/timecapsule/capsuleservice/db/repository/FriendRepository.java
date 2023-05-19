package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
}
