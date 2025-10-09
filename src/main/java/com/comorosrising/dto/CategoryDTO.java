package com.comorosrising.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        Long id,
        @NotBlank(message = "Category name must be provided")
        String categoryName
) {
}
