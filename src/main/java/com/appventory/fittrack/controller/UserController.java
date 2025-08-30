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

import com.appventory.fittrack.dto.UserReportResponse;
import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.service.ReportService;
import com.appventory.fittrack.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {

	private final UserService userService;
	private final ReportService repoortService;

	@Operation(summary = "Create a new user", description = "Registers a new user in the system.")
	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
	}

	@Operation(summary = "Get all users", description = "Fetch paginated list of all users. Requires ADMIN role.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<Page<UserResponse>> getAllUsers(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return ResponseEntity.ok(userService.getAllUsers(pageable));
	}

	@Operation(summary = "Get a user by ID", description = "Fetch user details by ID.")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.getUserById(id));
	}

	@Operation(summary = "Update a user", description = "Update user details by ID.")
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
		return ResponseEntity.ok(userService.updateUser(id, request));
	}

	@Operation(summary = "Delete a user", description = "Delete user by ID.")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		String msg = userService.deleteUser(id);
		return ResponseEntity.ok(msg);
	}

	@GetMapping("/{id}/report")
	public ResponseEntity<UserReportResponse> getUserReport(@PathVariable Long id) {
		return ResponseEntity.ok(repoortService.generateUserReport(id));
	}
}
