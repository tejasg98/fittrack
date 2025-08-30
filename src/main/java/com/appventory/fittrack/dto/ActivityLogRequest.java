package com.appventory.fittrack.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ActivityLogRequest {
	@NotNull
	private LocalDate date;

	@NotBlank
	private String activityType;

	@PositiveOrZero
	private Integer durationMinutes;

	private Integer caloriesBurned;

	private String notes;

	@NotNull
	private Long userId;

	private Long workoutPlanId;
}
