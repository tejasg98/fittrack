package com.appventory.fittrack.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.dto.WorkoutPlanResponse;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.mapper.WorkoutPlanMapper;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.*;

class WorkoutPlanServiceTest {

	@Mock
	private WorkoutPlanRepository workoutPlanRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private WorkoutPlanServiceImpl workoutPlanService;

	private User user;
	private WorkoutPlan plan;
	private WorkoutPlanRequest planRequest;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		user = User.builder().id(1L).fullName("Rohit Sharma").email("rohit@test.com").build();
		planRequest = WorkoutPlanRequest.builder().title("Full Body Workout").description("Strength and cardio")
				.durationInWeeks(4).goal("Weight Loss").userId(1L).build();
		plan = WorkoutPlanMapper.toEntity(planRequest, user);
		plan.setId(1L);
	}

	@Test
	void createWorkoutPlanSuccess() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(plan);

		WorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(planRequest);

		assertNotNull(response);
		assertEquals("Full Body Workout", response.getTitle());
		verify(workoutPlanRepository, times(1)).save(any(WorkoutPlan.class));
	}

	@Test
	void createWorkoutPlanUserNotFound() {
		when(userRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> workoutPlanService.createWorkoutPlan(planRequest));
	}

	@Test
	void getAllWorkoutPlans() {
		Page<WorkoutPlan> page = new PageImpl<>(List.of(plan));
		when(workoutPlanRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

		Page<WorkoutPlanResponse> result = workoutPlanService.getAllWorkoutPlans(PageRequest.of(0, 10));

		assertEquals(1, result.getTotalElements());
	}

	@Test
	void getWorkoutPlanByIdSuccess() {
		when(workoutPlanRepository.findById(1L)).thenReturn(Optional.of(plan));

		WorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(1L);

		assertEquals("Full Body Workout", response.getTitle());
	}

	@Test
	void getWorkoutPlanByIdNotFound() {
		when(workoutPlanRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> workoutPlanService.getWorkoutPlanById(1L));
	}

	@Test
	void deleteWorkoutPlanSuccess() {
		when(workoutPlanRepository.existsById(1L)).thenReturn(true);
		doNothing().when(workoutPlanRepository).deleteById(1L);

		String msg = workoutPlanService.deleteWorkoutPlan(1L);
		assertTrue(msg.contains("deleted successfully"));
	}

	@Test
	void deleteWorkoutPlanNotFound() {
		when(workoutPlanRepository.existsById(1L)).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> workoutPlanService.deleteWorkoutPlan(1L));
	}
}
