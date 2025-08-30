package com.appventory.fittrack.mapper;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.dto.WorkoutPlanResponse;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;

public class WorkoutPlanMapper {
	private WorkoutPlanMapper() {}
	public static WorkoutPlan toEntity(WorkoutPlanRequest request, User owner) {
		return WorkoutPlan.builder().title(request.getTitle()).description(request.getDescription())
				.durationInWeeks(request.getDurationInWeeks()).goal(request.getGoal()).owner(owner).build();
	}

	public static WorkoutPlanResponse toResponse(WorkoutPlan entity) {
		return WorkoutPlanResponse.builder().id(entity.getId()).title(entity.getTitle())
				.description(entity.getDescription()).durationInWeeks(entity.getDurationInWeeks())
				.goal(entity.getGoal()).userId(entity.getOwner().getId()).userName(entity.getOwner().getFullName())
				.build();
	}
}
