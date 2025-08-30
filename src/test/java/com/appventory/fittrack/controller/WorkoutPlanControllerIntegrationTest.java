package com.appventory.fittrack.controller;

import com.appventory.fittrack.dto.WorkoutPlanRequest;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;
import com.appventory.fittrack.repository.WorkoutPlanRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import  org.springframework.security.test.context.support.WithMockUser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)  // 🚀 disables Spring Security filters
class WorkoutPlanControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WorkoutPlanRepository workoutPlanRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private User user;

	@BeforeEach
	void setup() {
		workoutPlanRepository.deleteAll();
		userRepository.deleteAll();

		user = User.builder().fullName("Virat Kohli").email("virat@test.com").password("virat123")
				.roles(Set.of("ROLE_USER")).build();
		user = userRepository.save(user);
	}

	@Test
	void createWorkoutPlanSuccess() throws Exception {
		WorkoutPlanRequest request = WorkoutPlanRequest.builder().title("Strength Training")
				.description("Upper body strength").durationInWeeks(6).goal("Muscle Gain").userId(user.getId())
				.build();

		mockMvc.perform(post("/v1/api/workout-plans").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.title").value("Strength Training"));
	}

	@Test
	@WithMockUser(username = "virat@test.com", roles = {"ADMIN"})
	void getWorkoutPlansWithPagination() throws Exception {
	    // Arrange: create a workout plan directly
	    WorkoutPlanRequest request = WorkoutPlanRequest.builder()
	            .title("Strength Training")
	            .description("Upper body strength")
	            .durationInWeeks(6)
	            .goal("Muscle Gain")
	            .userId(user.getId())
	            .build();

	    mockMvc.perform(post("/v1/api/workout-plans")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isCreated());

	    // Act + Assert: fetch with pagination
	    mockMvc.perform(get("/v1/api/workout-plans?page=0&size=10"))
	            .andDo(print())
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.content[0].title").value("Strength Training"));
	}


	@Test
	void deleteWorkoutPlanSuccess() throws Exception {
		WorkoutPlanRequest request = WorkoutPlanRequest.builder().title("Cardio Plan").description("Daily cardio")
				.durationInWeeks(4).goal("Fat Loss").userId(user.getId()).build();

		String json = objectMapper.writeValueAsString(request);

		String response = mockMvc
				.perform(post("/v1/api/workout-plans").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

		Long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(delete("/v1/api/workout-plans/{id}", id)).andExpect(status().isOk())
				.andExpect(content().string("Workout plan deleted successfully with id:" + id));
	}
}
