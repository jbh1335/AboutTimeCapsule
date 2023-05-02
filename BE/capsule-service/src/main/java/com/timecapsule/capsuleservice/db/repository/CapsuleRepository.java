package com.timecapsule.capsuleservice.db.repository;

import com.timecapsule.capsuleservice.db.entity.Capsule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapsuleRepository extends JpaRepository<Capsule, Integer> {
    Optional<Capsule> findById(int id);
    @Query(value = "SELECT c.* FROM capsule c " +
            "WHERE ST_Distance_Sphere(point(c.longitude, c.latitude), point(:longitude, :latitude)) <= 1000",
            nativeQuery = true)
    List<Capsule> findAroundCapsule(@Param("latitude") Double latitude,
                                    @Param("longitude") Double longitude);
}
