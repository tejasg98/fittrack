package com.appventory.fittrack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.appventory.fittrack.dto.UserReportResponse;
import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.service.ReportService;
import com.appventory.fittrack.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private ReportService reportService;

	private UserRequest validUserRequest;
	private UserResponse userResponse;
	private UserReportResponse userReportResponse;

	@BeforeEach
	void setUp() {
		validUserRequest = UserRequest.builder().fullName("testuser").email("test@example.com").password("password123")
				.build();

		userResponse = UserResponse.builder().id(1L).fullName("testuser").email("test@example.com").build();

		userReportResponse = UserReportResponse.builder().userId(1L).fullName("testuser").build();
	}

	@Test
	@WithMockUser(roles = "USER")
	void createUser_ReturnsCreated() throws Exception {
		when(userService.createUser(any(UserRequest.class))).thenReturn(userResponse);

		mockMvc.perform(post("/v1/api/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validUserRequest))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.fullName").value("testuser"));
	}

	@Test
	void getAllUsers_Unauthenticated_ReturnsUnauthorized() throws Exception {
		mockMvc.perform(get("/v1/api/users?page=0&size=10")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getAllUsers_Admin_ReturnsOk() throws Exception {
		when(userService.getAllUsers(any(Pageable.class)))
				.thenReturn(new PageImpl<>(Collections.singletonList(userResponse), PageRequest.of(0, 10), 1));

		mockMvc.perform(get("/v1/api/users?page=0&size=10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].id").value(1L))
				.andExpect(jsonPath("$.content[0].fullName").value("testuser"));
	}

	@Test
	@WithMockUser
	void getUser_ReturnsOk() throws Exception {
		when(userService.getUserById(1L)).thenReturn(userResponse);

		mockMvc.perform(get("/v1/api/users/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	@WithMockUser
	void updateUser_ReturnsOk() throws Exception {
		when(userService.updateUser(any(Long.class), any(UserRequest.class))).thenReturn(userResponse);

		mockMvc.perform(put("/v1/api/users/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(validUserRequest))).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	@WithMockUser
	void deleteUser_ReturnsOk() throws Exception {
		when(userService.deleteUser(1L)).thenReturn("User deleted");

		mockMvc.perform(delete("/v1/api/users/1")).andExpect(status().isOk())
				.andExpect(content().string("User deleted"));
	}

	@Test
	@WithMockUser(roles = "USER")
	void getUserReport_ReturnsOk() throws Exception {
		when(reportService.generateUserReport(1L)).thenReturn(userReportResponse);

		mockMvc.perform(get("/v1/api/users/1/report")).andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(1L));
	}
}
