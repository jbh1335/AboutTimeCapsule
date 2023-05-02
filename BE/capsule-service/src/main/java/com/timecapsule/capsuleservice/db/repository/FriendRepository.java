package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    boolean existsByIsAcceptedAndFromMemberIdAndToMemberId(boolean isAccepted, int fromMemberId, int toMemberId);
    List<Friend> findAllByFromMemberIdOrToMemberIdAndIsAcceptedTrue(int memberId1, int memberId2);
}
