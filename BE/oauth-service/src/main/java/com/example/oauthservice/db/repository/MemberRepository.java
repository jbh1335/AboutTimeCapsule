package com.example.oauthservice.db.repository;

import com.example.oauthservice.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    String findById(int id);
}
