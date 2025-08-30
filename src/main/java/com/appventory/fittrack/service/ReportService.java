package com.appventory.fittrack.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appventory.fittrack.dto.ActivityLogSummaryResponse;
import com.appventory.fittrack.dto.UserReportResponse;
import com.appventory.fittrack.dto.WorkoutPlanSummaryResponse;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.repository.ActivityLogRepository;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

	private final UserRepository userRepository;
	private final ActivityLogRepository activityLogRepository;
	private final WorkoutPlanRepository workoutPlanRepository;

	/**
	 * Generates a comprehensive report for a specific user including:
	 * - User profile information
	 * - Activity log history
	 * - Workout plans
	 * 
	 * @param userId the ID of the user to generate the report for
	 * @return UserReportResponse containing all user data and activities
	 * @throws ResourceNotFoundException if the user is not found
	 */
	public UserReportResponse generateUserReport(Long userId) {
		log.info("Generating user report for user ID: {}", userId);
		
		// Fetch and validate user exists
		var user = userRepository.findById(userId)
				.orElseThrow(() -> {
					log.error("User not found with ID: {}", userId);
					return new ResourceNotFoundException("User not found with ID: " + userId);
				});
		log.debug("Found user: {} ({})", user.getFullName(), user.getEmail());

		// Fetch user's activity logs
		log.debug("Fetching activity logs for user ID: {}", userId);
		var activities = activityLogRepository.findByUserId(userId).stream()
				.map(a -> ActivityLogSummaryResponse.builder()
						.id(a.getId())
						.date(a.getDate())
						.activityType(a.getActivityType())
						.durationMinutes(a.getDurationMinutes())
						.caloriesBurned(a.getCaloriesBurned())
						.notes(a.getNotes())
						.build())
				.toList();
		log.info("Retrieved {} activity logs for user ID: {}", activities.size(), userId);

		// Fetch user's workout plans
		log.debug("Fetching workout plans for user ID: {}", userId);
		List<WorkoutPlanSummaryResponse> workoutPlans = workoutPlanRepository.findByOwnerId(userId).stream()
				.map(w -> WorkoutPlanSummaryResponse.builder()
						.id(w.getId())
						.title(w.getTitle())
						.description(w.getDescription())
						.durationInWeeks(w.getDurationInWeeks())
						.goal(w.getGoal())
						.build())
				.toList();
		log.info("Retrieved {} workout plans for user ID: {}", workoutPlans.size(), userId);

		// Build and return the comprehensive report
		log.debug("Building user report response for user ID: {}", userId);
		UserReportResponse response = UserReportResponse.builder()
				.userId(user.getId())
				.fullName(user.getFullName())
				.email(user.getEmail())
				.dateOfBirth(user.getDateOfBirth())
				.weight(user.getWeight())
				.height(user.getHeight())
				.activities(activities)
				.workoutPlans(workoutPlans)
				.build();
		
		log.info("Successfully generated report for user ID: {} ({} activities, {} workout plans)", 
				userId, activities.size(), workoutPlans.size());
		
		return response;
	}
}