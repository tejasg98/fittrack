package com.appventory.fittrack.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;
import com.appventory.fittrack.service.ActivityLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/api/activity-logs")
@RequiredArgsConstructor
@Tag(name = "Activity Logs", description = "APIs for managing user activity logs")
public class ActivityLogController {

	private final ActivityLogService activityLogService;

	@Operation(summary = "Create activity log", description = "Creates a new activity log for the current user.")
	@PostMapping
	public ResponseEntity<ActivityLogResponse> create(@Valid @RequestBody ActivityLogRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(activityLogService.createActivityLog(request));
	}

	@Operation(summary = "Get all activity logs", description = "Fetch paginated list of all activity logs. Requires ADMIN role.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<ActivityLogResponse>> getAll(@PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(activityLogService.getAllActivityLogs(pageable));
	}

	@Operation(summary = "Get activity log by ID", description = "Fetch a specific activity log by its ID.")
	@GetMapping("/{id}")
	public ResponseEntity<ActivityLogResponse> getById(@PathVariable Long id) {
		return ResponseEntity.ok(activityLogService.getActivityLogById(id));
	}

	@Operation(summary = "Update activity log", description = "Update an activity log by its ID.")
	@PutMapping("/{id}")
	public ResponseEntity<ActivityLogResponse> update(@PathVariable Long id,
			@Valid @RequestBody ActivityLogRequest request) {
		return ResponseEntity.ok(activityLogService.updateActivityLog(id, request));
	}

	@Operation(summary = "Delete activity log", description = "Delete an activity log by ID.")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		return ResponseEntity.ok(activityLogService.deleteActivityLog(id));
	}
}
