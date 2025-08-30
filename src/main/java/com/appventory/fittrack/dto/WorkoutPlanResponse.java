package com.appventory.fittrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkoutPlanResponse {
	private Long id;
	private String title;
	private String description;
	private Integer durationInWeeks;
	private String goal;
	private Long userId;
	private String userName;
}
