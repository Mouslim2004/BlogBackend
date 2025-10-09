package com.comorosrising.dto;

import com.comorosrising.entity.PostStatus;
import com.comorosrising.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostsDTO(
        @NotNull(message = "ID cannot be null")
        Long id,
        @NotBlank(message = "Title is required")
        @Size(min = 3, max = 100, message = "Title must be 3 and 100 characters")
        String title,
        @NotBlank(message = "Content is mandatory")
        @Size(min = 10, message = "Content must be at least 10 characters")
        String content,
        PostStatus status,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Category ID is required")
        Long categoryId

        //UserOutputDTO user,
        //CategoryDTO category
) {
}
