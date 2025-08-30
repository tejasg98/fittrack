package com.appventory.fittrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.dto.WorkoutPlanResponse;

public interface WorkoutPlanService {
	WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request);

	Page<WorkoutPlanResponse> getAllWorkoutPlans(Pageable pageable);

	WorkoutPlanResponse getWorkoutPlanById(Long id);

	WorkoutPlanResponse updateWorkoutPlan(Long id, WorkoutPlanRequest request);

	String deleteWorkoutPlan(Long id);
}
