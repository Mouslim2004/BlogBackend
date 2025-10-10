package com.comorosrising.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCommentDTO(
        @NotBlank(message = "Content is required")
        @Size(min = 1, max = 500, message = "Content must be between 1 and 500 characters")
        String content
) {}