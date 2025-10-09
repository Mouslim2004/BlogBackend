package com.comorosrising.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UserDTO(
        Long id,
        @NotBlank(message = "Name must be provided")
        String name,
        @Email(message = "Must be a valid email address")
        @NotBlank(message = "Email must be provided")
        String email,
        @NotBlank(message = "Password must be provided")
        @Size(min = 5, max = 15, message = "Password must be between 5 and 15 characters")
        String password,
        @Size(max = 50, message = "Bio must be less than or equal to 50 characters")
        String bio,
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(pattern = "MM-dd-yyyy")
        LocalDate dateOfBirth
) {
}
