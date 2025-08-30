package com.appventory.fittrack.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReportResponse {
	private Long userId;
	private String fullName;
	private String email;
	private LocalDate dateOfBirth;
	private Double weight;
	private Double height;
	private List<ActivityLogSummaryResponse> activities;
	private List<WorkoutPlanSummaryResponse> workoutPlans;
}
