package com.appventory.fittrack.dto;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 100)
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Past
    private LocalDate dateOfBirth;

    @Positive
    private Double weight;

    @Positive
    private Double height;

    private Set<String> roles;
}
