package com.appventory.fittrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutPlanRequest {
	@NotBlank
	private String title;

	private String description;

	@NotNull
	private Integer durationInWeeks;

	@NotBlank
	private String goal;

	@NotNull
	private Long userId;
}
