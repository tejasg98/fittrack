package com.appventory.fittrack.mapper;

import java.util.HashSet;
import java.util.Set;

import com.appventory.fittrack.dto.UserRequest;
import com.appventory.fittrack.dto.UserResponse;
import com.appventory.fittrack.model.User;

public final class UserMapper {

	private UserMapper() {
	}

	public static User toEntity(UserRequest dto) {
		if (dto == null)
			return null;

		return User.builder().fullName(dto.getFullName()).email(dto.getEmail()).password(dto.getPassword()) 
				.dateOfBirth(dto.getDateOfBirth()).weight(dto.getWeight()).height(dto.getHeight())
				.roles(resolveRoles(dto.getRoles())).build();
	}

	public static UserResponse toResponse(User entity) {
		if (entity == null)
			return null;

		return UserResponse.builder().id(entity.getId()).fullName(entity.getFullName()).email(entity.getEmail())
				.dateOfBirth(entity.getDateOfBirth()).weight(entity.getWeight()).height(entity.getHeight())
				.roles(entity.getRoles()).createdAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null) // format if needed
				.updatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null).build();
	}

	private static Set<String> resolveRoles(Set<String> roles) {
		if (roles == null || roles.isEmpty()) {
			return Set.of("ROLE_USER"); // immutable default
		}
		return new HashSet<>(roles); 
	}
}
