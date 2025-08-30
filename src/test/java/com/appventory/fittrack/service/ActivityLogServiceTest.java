package com.appventory.fittrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.model.ActivityLog;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.model.WorkoutPlan;
import com.appventory.fittrack.repository.ActivityLogRepository;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

class ActivityLogServiceTest {

    @Mock
    private ActivityLogRepository activityLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @InjectMocks
    private ActivityLogServiceImpl activityLogService;

    private User user;
    private WorkoutPlan workoutPlan;
    private ActivityLog activityLog;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        workoutPlan = new WorkoutPlan();
        workoutPlan.setId(1L);

        activityLog = new ActivityLog();
        activityLog.setId(1L);
        activityLog.setUser(user);
        activityLog.setWorkoutPlan(workoutPlan);
        activityLog.setActivityType("Running");
        activityLog.setDate(LocalDate.now());
        activityLog.setDurationMinutes(30);
    }

    @Test
    void createActivityLog_ShouldSaveAndReturnResponse() {
    	ActivityLogRequest request = ActivityLogRequest.builder()
    	        .userId(user.getId())
    	        .workoutPlanId(workoutPlan.getId())
    	        .date(LocalDate.now())
    	        .activityType("Running")
    	        .durationMinutes(30)
    	        .caloriesBurned(200)
    	        .notes("Morning run")
    	        .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(workoutPlanRepository.findById(workoutPlan.getId())).thenReturn(Optional.of(workoutPlan));
        when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(activityLog);

        ActivityLogResponse response = activityLogService.createActivityLog(request);

        assertThat(response).isNotNull();
        assertThat(response.getActivityType()).isEqualTo("Running");
        verify(activityLogRepository, times(1)).save(any(ActivityLog.class));
    }

    @Test
    void createActivityLog_ShouldThrow_WhenUserNotFound() {
    	ActivityLogRequest request = ActivityLogRequest.builder()
    	        .userId(user.getId())
    	        .workoutPlanId(workoutPlan.getId())
    	        .date(LocalDate.now())
    	        .activityType("Running")
    	        .durationMinutes(30)
    	        .caloriesBurned(200)
    	        .notes("Morning run")
    	        .build();


        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityLogService.createActivityLog(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getAllActivityLogs_ShouldReturnPage() {
        Page<ActivityLog> page = new PageImpl<>(List.of(activityLog));
        when(activityLogRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ActivityLogResponse> result = activityLogService.getAllActivityLogs(Pageable.unpaged());

        assertThat(result).hasSize(1);
        verify(activityLogRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getActivityLogById_ShouldReturnResponse() {
        when(activityLogRepository.findById(1L)).thenReturn(Optional.of(activityLog));

        ActivityLogResponse response = activityLogService.getActivityLogById(1L);

        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void getActivityLogById_ShouldThrow_WhenNotFound() {
        when(activityLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityLogService.getActivityLogById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateActivityLog_ShouldUpdate_WhenUserIsOwner() {
    	ActivityLogRequest request = ActivityLogRequest.builder()
    	        .userId(user.getId())
    	        .workoutPlanId(workoutPlan.getId())
    	        .date(LocalDate.now())
    	        .activityType("Running")
    	        .durationMinutes(30)
    	        .caloriesBurned(200)
    	        .notes("Morning run")
    	        .build();


        // Mock Security Context
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(activityLogRepository.findById(1L)).thenReturn(Optional.of(activityLog));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(workoutPlanRepository.findById(workoutPlan.getId())).thenReturn(Optional.of(workoutPlan));
        when(activityLogRepository.save(any(ActivityLog.class))).thenReturn(activityLog);

        ActivityLogResponse response = activityLogService.updateActivityLog(1L, request);

        assertThat(response.getActivityType()).isEqualTo("Running");
    }

    @Test
    void updateActivityLog_ShouldThrow_WhenNotOwner() {
    	ActivityLogRequest request = ActivityLogRequest.builder()
    	        .userId(user.getId())
    	        .workoutPlanId(workoutPlan.getId())
    	        .date(LocalDate.now())
    	        .activityType("Running")
    	        .durationMinutes(30)
    	        .caloriesBurned(200)
    	        .notes("Morning run")
    	        .build();


        // Mock Security Context
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(auth.getName()).thenReturn("other@example.com");
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(activityLogRepository.findById(1L)).thenReturn(Optional.of(activityLog));

        assertThatThrownBy(() -> activityLogService.updateActivityLog(1L, request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Access denied");
    }

    @Test
    void deleteActivityLog_ShouldDelete_WhenExists() {
        when(activityLogRepository.existsById(1L)).thenReturn(true);

        String result = activityLogService.deleteActivityLog(1L);

        assertThat(result).contains("deleted successfully");
        verify(activityLogRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteActivityLog_ShouldThrow_WhenNotFound() {
        when(activityLogRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> activityLogService.deleteActivityLog(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
