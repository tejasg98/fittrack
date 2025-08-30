package com.appventory.fittrack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.appventory.fittrack.dto.ActivityLogRequest;
import com.appventory.fittrack.dto.ActivityLogResponse;
import com.appventory.fittrack.service.ActivityLogService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ActivityLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActivityLogService activityLogService;

    @Autowired
    private ObjectMapper objectMapper;	

    private ActivityLogResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleResponse = ActivityLogResponse.builder()
                .id(1L)
                .activityType("Running")
                .date(LocalDate.now())
                .durationMinutes(30)
                .caloriesBurned(200)
                .notes("Morning Run")
                .build();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testCreateActivityLog() throws Exception {
        ActivityLogRequest request = ActivityLogRequest.builder()
                .userId(1L)
                .workoutPlanId(2L)
                .date(LocalDate.now())
                .activityType("Running")
                .durationMinutes(30)
                .caloriesBurned(200)
                .notes("Morning Run")
                .build();

        Mockito.when(activityLogService.createActivityLog(any(ActivityLogRequest.class)))
                .thenReturn(sampleResponse);

        mockMvc.perform(post("/v1/api/activity-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.activityType").value("Running"))
                .andExpect(jsonPath("$.caloriesBurned").value(200));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllActivityLogs() throws Exception {
        Mockito.when(activityLogService.getAllActivityLogs(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponse)));

        mockMvc.perform(get("/v1/api/activity-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].activityType").value("Running"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetActivityLogById() throws Exception {
        Mockito.when(activityLogService.getActivityLogById(1L)).thenReturn(sampleResponse);

        mockMvc.perform(get("/v1/api/activity-logs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityType").value("Running"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testUpdateActivityLog() throws Exception {
        ActivityLogRequest updateRequest = ActivityLogRequest.builder()
                .userId(1L)
                .workoutPlanId(2L)
                .date(LocalDate.now())
                .activityType("Cycling")
                .durationMinutes(45)
                .caloriesBurned(350)
                .notes("Evening Ride")
                .build();

        ActivityLogResponse updatedResponse = ActivityLogResponse.builder()
                .id(1L)
                .activityType("Cycling")
                .date(LocalDate.now())
                .durationMinutes(45)
                .caloriesBurned(350)
                .notes("Evening Ride")
                .build();

        Mockito.when(activityLogService.updateActivityLog(eq(1L), any(ActivityLogRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/v1/api/activity-logs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityType").value("Cycling"))
                .andExpect(jsonPath("$.durationMinutes").value(45));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testDeleteActivityLog() throws Exception {
        Mockito.when(activityLogService.deleteActivityLog(1L))
                .thenReturn("Activity log deleted successfully with id: 1");

        mockMvc.perform(delete("/v1/api/activity-logs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Activity log deleted successfully with id: 1"));
    }
}
