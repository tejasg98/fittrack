package com.appventory.fittrack.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.dto.WorkoutPlanResponse;
import com.appventory.fittrack.service.WorkoutPlanService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createWorkoutPlan(
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.createWorkoutPlan(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<WorkoutPlanResponse>> getAllWorkoutPlans(
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return ResponseEntity.ok(workoutPlanService.getAllWorkoutPlans(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlan(@PathVariable Long id) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlanById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updateWorkoutPlan(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity.ok(workoutPlanService.updateWorkoutPlan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkoutPlan(@PathVariable Long id) {
        String msg = workoutPlanService.deleteWorkoutPlan(id);
        return ResponseEntity.ok(msg);
    }
}
