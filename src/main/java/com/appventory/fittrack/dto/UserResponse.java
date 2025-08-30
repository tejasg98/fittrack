package com.appventory.fittrack.dto;


import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private Double weight;
    private Double height;
    private Set<String> roles;
    private String createdAt;
    private String updatedAt;
}
