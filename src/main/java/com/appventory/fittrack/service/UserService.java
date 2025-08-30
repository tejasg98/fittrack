package com.appventory.fittrack.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;

@Service
public interface UserService {
	UserResponse createUser(UserRequest request);

	Page<UserResponse> getAllUsers(Pageable pageable);

	UserResponse getUserById(Long id);

	UserResponse updateUser(Long id, UserRequest request);

	String deleteUser(Long id);

}
