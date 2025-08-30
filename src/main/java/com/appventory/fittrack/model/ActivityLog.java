package com.appventory.fittrack.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityLog extends AbstractModel {
	@NotNull
	@Column(nullable = false)
	private LocalDate date;

	@NotBlank
	@Column(nullable = false)
	private String activityType;

	@PositiveOrZero
	@Column(nullable = false)
	private Integer durationMinutes;

	private Integer caloriesBurned;

	private String notes;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workout_plan_id")
	private WorkoutPlan workoutPlan;

}
