package com.comorosrising.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginDTO(
        @Email
        @NotEmpty(message = "Email must be provided")
        String email,
        @NotEmpty(message = "Password must be provided")
        String password
) {
}
