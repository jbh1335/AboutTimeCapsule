package com.timecapsule.memberservice.db.repository;

import com.timecapsule.memberservice.db.entity.Friend;
import com.timecapsule.memberservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Integer> {
    Optional<Friend> findById(int id);
    void deleteById(int id);
    @Query("SELECT f FROM Friend f WHERE (f.fromMember.id = :memberId1 AND f.toMember.id = :memberId2) OR (f.fromMember.id = :memberId2 AND f.toMember.id = :memberId1)")
    Optional<Friend> findByMemberIds(int memberId1, int memberId2);
    Optional<Friend> findByIsAcceptedFalseAndFromMemberIdAndToMemberId(int fromMemberId, int toMemberId);
}
