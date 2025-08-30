package com.appventory.fittrack.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class User extends AbstractModel {

	@NotBlank
	@Size(min = 3, max = 100)
	@Column(nullable = false)
	private String fullName;

	@Email
	@NotBlank
	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank
	@Size(min = 8, message = "Password must be at least 8 characters")
	@Column(nullable = false)
	private String password;

	@Past
	private LocalDate dateOfBirth;

	@Positive
	private Double weight; // kg

	@Positive
	private Double height; // cm

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	@Builder.Default
	private Set<String> roles = new HashSet<>();
}
