package com.appventory.fittrack.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityLogResponse {
    private Long id;
    private LocalDate date;
    private String activityType;
    private Integer durationMinutes;
    private Integer caloriesBurned;
    private String notes;
    private Long userId;
    private String userName;
    private Long workoutPlanId;
    private String workoutPlanName;
}

