package com.appventory.fittrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;
import com.appventory.fittrack.exception.AccessDeniedException;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.mapper.ActivityLogMapper;
import com.appventory.fittrack.model.ActivityLog;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;
import com.appventory.fittrack.repository.ActivityLogRepository;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class ActivityLogServiceImpl implements ActivityLogService {

	private static final String ACTIVITY_LOG_NOT_FOUND_WITH_ID = "ActivityLog not found with ID: {}";
	private final ActivityLogRepository activityLogRepository;
	private final UserRepository userRepository;
	private final WorkoutPlanRepository workoutPlanRepository;

	@Override
	public ActivityLogResponse createActivityLog(ActivityLogRequest request) {
		log.info("Creating new activity log for user ID: {}", request.getUserId());
		
		// Validate and fetch user
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> {
					log.error("User not found with ID: {}", request.getUserId());
					return new ResourceNotFoundException("User not found with id: " + request.getUserId());
				});

		WorkoutPlan workoutPlan = null;
		if (request.getWorkoutPlanId() != null) {
			log.debug("Fetching workout plan with ID: {}", request.getWorkoutPlanId());
			workoutPlan = workoutPlanRepository.findById(request.getWorkoutPlanId())
					.orElseThrow(() -> {
						log.error("WorkoutPlan not found with ID: {}", request.getWorkoutPlanId());
						return new ResourceNotFoundException(
								"WorkoutPlan not found with id: " + request.getWorkoutPlanId());
					});
		}

		ActivityLog entity = ActivityLogMapper.toEntity(request, user, workoutPlan);
		ActivityLog saved = activityLogRepository.save(entity);
		log.info("Activity log created successfully with ID: {}", saved.getId());
		
		return ActivityLogMapper.toResponse(saved);
	}

	@Override
	public Page<ActivityLogResponse> getAllActivityLogs(Pageable pageable) {
		log.debug("Fetching all activity logs with pagination: {}", pageable);
		Page<ActivityLogResponse> response = activityLogRepository.findAll(pageable)
				.map(ActivityLogMapper::toResponse);
		log.info("Retrieved {} activity logs", response.getNumberOfElements());
		return response;
	}

	@Override
	public ActivityLogResponse getActivityLogById(Long id) {
		log.debug("Fetching activity log with ID: {}", id);
		ActivityLog activityLog = activityLogRepository.findById(id)
				.orElseThrow(() -> {
					log.error(ACTIVITY_LOG_NOT_FOUND_WITH_ID, id);
					return new ResourceNotFoundException("ActivityLog not found with id: " + id);
				});
		log.info("Successfully retrieved activity log with ID: {}", id);
		return ActivityLogMapper.toResponse(activityLog);
	}

	@Override
	public ActivityLogResponse updateActivityLog(Long id, ActivityLogRequest request) {
		log.info("Updating activity log with ID: {}", id);
		
		// Check if activity log exists
		ActivityLog existing = activityLogRepository.findById(id)
				.orElseThrow(() -> {
					log.error(ACTIVITY_LOG_NOT_FOUND_WITH_ID, id);
					return new ResourceNotFoundException("ActivityLog not found with id: " + id);
				});
		
		// Check ownership
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = auth.getName();
		if (!existing.getUser().getEmail().equals(currentEmail)) {
			log.warn("User {} attempted to update activity log owned by {}", 
					currentEmail, existing.getUser().getEmail());
			throw new AccessDeniedException("Access denied: not your activity log");
		}

		// Update fields
		log.debug("Updating activity log fields for ID: {}", id);
		existing.setDate(request.getDate());
		existing.setActivityType(request.getActivityType());
		existing.setDurationMinutes(request.getDurationMinutes());
		existing.setCaloriesBurned(request.getCaloriesBurned());
		existing.setNotes(request.getNotes());

		// Update user if provided
		if (request.getUserId() != null) {
			log.debug("Updating user reference for activity log ID: {}", id);
			User user = userRepository.findById(request.getUserId())
					.orElseThrow(() -> {
						log.error("User not found with ID: {}", request.getUserId());
						return new ResourceNotFoundException("User not found with id: " + request.getUserId());
					});
			existing.setUser(user);
		}

		// Update workout plan if provided
		if (request.getWorkoutPlanId() != null) {
			log.debug("Updating workout plan reference for activity log ID: {}", id);
			WorkoutPlan workoutPlan = workoutPlanRepository.findById(request.getWorkoutPlanId())
					.orElseThrow(() -> {
						log.error("WorkoutPlan not found with ID: {}", request.getWorkoutPlanId());
						return new ResourceNotFoundException(
								"WorkoutPlan not found with id: " + request.getWorkoutPlanId());
					});
			existing.setWorkoutPlan(workoutPlan);
		}

		// Save updated entity
		ActivityLog updated = activityLogRepository.save(existing);
		log.info("Activity log with ID: {} updated successfully", id);
		
		return ActivityLogMapper.toResponse(updated);
	}

	@Override
	public String deleteActivityLog(Long id) {
		log.info("Deleting activity log with ID: {}", id);
		
		// Check if activity log exists
		if (!activityLogRepository.existsById(id)) {
			log.error(ACTIVITY_LOG_NOT_FOUND_WITH_ID, id);
			throw new ResourceNotFoundException("ActivityLog not found with id: " + id);
		}
		
		// Delete the activity log
		activityLogRepository.deleteById(id);
		log.info("Activity log with ID: {} deleted successfully", id);
		
		return "Activity log deleted successfully with id: " + id;
	}
}