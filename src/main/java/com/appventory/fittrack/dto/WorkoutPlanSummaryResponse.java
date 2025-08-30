package com.appventory.fittrack.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WorkoutPlanSummaryResponse {
    private Long id;
    private String title;
    private String description;
    private Integer durationInWeeks;
    private String goal;
}
