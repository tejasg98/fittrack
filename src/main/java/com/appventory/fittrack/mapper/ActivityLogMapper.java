package com.appventory.fittrack.mapper;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;
import com.appventory.fittrack.model.ActivityLog;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;

public class ActivityLogMapper {

	private ActivityLogMapper() {}
	public static ActivityLog toEntity(ActivityLogRequest request, User user, WorkoutPlan workoutPlan) {
		return ActivityLog.builder().date(request.getDate()).activityType(request.getActivityType())
				.durationMinutes(request.getDurationMinutes()).caloriesBurned(request.getCaloriesBurned())
				.notes(request.getNotes()).user(user).workoutPlan(workoutPlan).build();
	}

	public static ActivityLogResponse toResponse(ActivityLog log) {
		return ActivityLogResponse.builder().id(log.getId()).date(log.getDate()).activityType(log.getActivityType())
				.durationMinutes(log.getDurationMinutes()).caloriesBurned(log.getCaloriesBurned()).notes(log.getNotes())
				.userId(log.getUser().getId()).userName(log.getUser().getFullName())
				.workoutPlanId(log.getWorkoutPlan() != null ? log.getWorkoutPlan().getId() : null)
				.workoutPlanName(log.getWorkoutPlan() != null ? log.getWorkoutPlan().getTitle() : null).build();
	}
}
