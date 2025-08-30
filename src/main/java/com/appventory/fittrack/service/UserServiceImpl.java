package com.appventory.fittrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.exception.AccessDeniedException;
import com.appventory.fittrack.exception.DuplicateResourceException;
import com.appventory.fittrack.exception.ResourceNotFoundException;
import com.appventory.fittrack.mapper.UserMapper;
import com.appventory.fittrack.model.User;
import com.appventory.fittrack.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponse createUser(UserRequest request) {
		log.info("Creating user with email: {}", request.getEmail());

		if (userRepository.existsByEmail(request.getEmail())) {
			log.warn("Attempted to create duplicate user with email: {}", request.getEmail());
			throw new DuplicateResourceException("Email already exists!");
		}

		User user = UserMapper.toEntity(request);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User saved = userRepository.save(user);

		log.info("User created successfully with id: {}", saved.getId());
		return UserMapper.toResponse(saved);
	}

	@Override
	public Page<UserResponse> getAllUsers(Pageable pageable) {
		log.info("Fetching all users with pagination: page={}, size={}", pageable.getPageNumber(),
				pageable.getPageSize());
		return userRepository.findAll(pageable).map(UserMapper::toResponse);
	}

	@Override
	public UserResponse getUserById(Long id) {
		log.info("Fetching user with id: {}", id);
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return UserMapper.toResponse(user);
	}

	@Override
	public UserResponse updateUser(Long id, UserRequest request) {
		log.info("Updating user with id: {}", id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String currentEmail = auth.getName();

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

		User existing = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		// Only owner or ADMIN can update
		boolean isOwner = existing.getEmail().equals(currentEmail);
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"));

		if (!isOwner && !isAdmin) {
			throwException(existing.getEmail(),currentEmail);
		}
		user.setFullName(request.getFullName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setDateOfBirth(request.getDateOfBirth());
		user.setWeight(request.getWeight());
		user.setHeight(request.getHeight());
		user.setRoles(request.getRoles() == null ? user.getRoles() : request.getRoles());

		User updated = userRepository.save(user);
		log.info("User updated successfully with id: {}", updated.getId());
		return UserMapper.toResponse(updated);
	}

	private void throwException(String userMail, String currentEmail) {
		try {
			throw new AccessDeniedException("Access denied: you cannot modify another user");
		} catch (Exception e) {
			log.error("Access denied: you cannot modify another user "+userMail+" "+currentEmail,e);
		}
	}

	@Override
	public String deleteUser(Long id) {
		log.info("Deleting user with id: {}", id);
		if (!userRepository.existsById(id)) {
			log.warn("User not found with id: {}", id);
			throw new ResourceNotFoundException("User not found with id: " + id);
		}
		userRepository.deleteById(id);
		String msg = "User deleted successfully with id:" + id;
		log.info(msg + " {}", id);
		return msg;
	}

	
}
