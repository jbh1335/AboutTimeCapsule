package com.timecapsule.memberservice.db.repository;

import com.timecapsule.memberservice.db.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAllByMemberId(int memberId);
}
