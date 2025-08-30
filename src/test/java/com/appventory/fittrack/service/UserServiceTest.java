package com.appventory.fittrack.service;

import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
	private PasswordEncoder passwordEncoder;

	private UserRequest request;
	private User user;

	@BeforeEach
	void setUp() {
		request = UserRequest.builder().fullName("John Doe").email("john@example.com").password("password123")
				.dateOfBirth(LocalDate.of(1995, 5, 20)).weight(70.0).height(175.0).roles(Set.of("USER")).build();

		user = User.builder().id(1L).fullName("John Doe").email("john@example.com").password("hashedpw")
				.dateOfBirth(LocalDate.of(1995, 5, 20)).weight(70.0).height(175.0).roles(Set.of("USER")).build();
	}

	@Test
	void testCreateUser() {
		// 🔹 Mock password encoder
		when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

		// 🔹 Mock repository save
		when(userRepository.save(any(User.class))).thenReturn(user);

		// 🔹 Call service
		UserResponse response = userService.createUser(request);

		// 🔹 Assertions
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getEmail()).isEqualTo("john@example.com");

		verify(passwordEncoder, times(1)).encode("password123"); // Ensure encoder was used
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	void testGetUserById() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		UserResponse response = userService.getUserById(1L);

		assertThat(response.getFullName()).isEqualTo("John Doe");
		assertThat(response.getWeight()).isEqualTo(70.0);
	}

	@Test
	void testGetAllUsers() {
		when(userRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(user)));

		Page<UserResponse> page = userService.getAllUsers(PageRequest.of(0, 5));

		assertThat(page.getContent()).hasSize(1);
		assertThat(page.getContent().get(0).getId()).isEqualTo(1L);
	}

	@Test
	void testUpdateUser() {
		// 🔹 Mock Authentication
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("john@example.com");

		SecurityContext securityContext = mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);

		SecurityContextHolder.setContext(securityContext);

		// 🔹 Mock repo
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(userRepository.save(any(User.class))).thenReturn(user);

		// 🔹 Call service
		UserResponse updated = userService.updateUser(1L, request);

		assertThat(updated.getFullName()).isEqualTo("John Doe");
		verify(userRepository, times(1)).save(any(User.class));

		// 🔹 Cleanup (important for isolation across tests!)
		SecurityContextHolder.clearContext();
	}

	@Test
	void testDeleteUser() {
		when(userRepository.existsById(1L)).thenReturn(true);
		doNothing().when(userRepository).deleteById(1L);

		String msg = userService.deleteUser(1L);

		assertThat(msg).contains("User deleted successfully with id");
		verify(userRepository, times(1)).deleteById(1L);
	}
}
