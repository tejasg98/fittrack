package com.appventory.fittrack.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutReportResponse {
    private Long userId;
    private String username;
    private List<WorkoutPlanResponse> workouts;
}