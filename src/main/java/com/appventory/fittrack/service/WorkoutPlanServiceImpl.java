package com.appventory.fittrack.service;

import java.nio.file.AccessDeniedException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.dto.WorkoutPlanResponse;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.mapper.WorkoutPlanMapper;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkoutPlanServiceImpl implements WorkoutPlanService {

	private final WorkoutPlanRepository workoutPlanRepository;
	private final UserRepository userRepository;

	@Override
	public WorkoutPlanResponse createWorkoutPlan(WorkoutPlanRequest request) {
		log.info("Creating workout plan: {}", request.getTitle());
		User owner = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
		WorkoutPlan plan = WorkoutPlanMapper.toEntity(request, owner);
		WorkoutPlan saved = workoutPlanRepository.save(plan);
		log.info("Workout plan created with id: {}", saved.getId());
		return WorkoutPlanMapper.toResponse(saved);
	}

	@Override
	public Page<WorkoutPlanResponse> getAllWorkoutPlans(Pageable pageable) {
		log.info("Fetching all workout plans: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
		return workoutPlanRepository.findAll(pageable).map(WorkoutPlanMapper::toResponse);
	}

	@Override
	public WorkoutPlanResponse getWorkoutPlanById(Long id) {
		log.info("Fetching workout plan with id: {}", id);
		WorkoutPlan plan = workoutPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + id));
		return WorkoutPlanMapper.toResponse(plan);
	}

	@Override
	public WorkoutPlanResponse updateWorkoutPlan(Long id, WorkoutPlanRequest request) {
		log.info("Updating workout plan with id: {}", id);
		WorkoutPlan plan = workoutPlanRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Workout plan not found with id: " + id));

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = auth.getName();

		// Check ownership
		if (!plan.getOwner().getEmail().equals(currentEmail)) {
			throwException(plan, currentEmail);
		}
		plan.setTitle(request.getTitle());
		plan.setDescription(request.getDescription());
		plan.setDurationInWeeks(request.getDurationInWeeks());
		plan.setGoal(request.getGoal());

		if (!plan.getOwner().getId().equals(request.getUserId())) {
			User owner = userRepository.findById(request.getUserId()).orElseThrow(
					() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
			plan.setOwner(owner);
		}

		WorkoutPlan updated = workoutPlanRepository.save(plan);
		log.info("Workout plan updated with id: {}", updated.getId());
		return WorkoutPlanMapper.toResponse(updated);
	}

	private void throwException(WorkoutPlan plan, String currentEmail) {
		try {
			throw new AccessDeniedException("Access denied: not your workout plan");
		} catch (Exception e) {
			log.error("Access denied: not your workout plan"+plan.getOwner().getEmail()+" "+currentEmail,e);
		}
	}

	@Override
	public String deleteWorkoutPlan(Long id) {
		log.info("Deleting workout plan with id: {}", id);
		if (!workoutPlanRepository.existsById(id)) {
			throw new ResourceNotFoundException("Workout plan not found with id: " + id);
		}
		workoutPlanRepository.deleteById(id);
		String msg = "Workout plan deleted successfully with id:" + id;
		log.info(msg);
		return msg;
	}

}
