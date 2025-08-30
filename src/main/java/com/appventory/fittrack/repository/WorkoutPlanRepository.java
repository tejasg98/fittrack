package com.appventory.fittrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appventory.fittrack.model.WorkoutPlan;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {
    List<WorkoutPlan> findByOwnerId(Long ownerId); // not Optional, not Page
}
