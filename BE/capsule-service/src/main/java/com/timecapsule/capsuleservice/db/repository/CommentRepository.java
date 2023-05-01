package com.timecapsule.capsuleservice.db.repository;


import com.timecapsule.capsuleservice.db.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByMemoryId(int memoryId);
}
