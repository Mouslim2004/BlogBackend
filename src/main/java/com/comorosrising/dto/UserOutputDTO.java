package com.comorosrising.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserOutputDTO(
        Long id,
        String name,
        String email,
        String bio,
        LocalDate dateOfBirth,
        LocalDateTime joinedAt
) {
}
