package com.comorosrising.dto;

import com.comorosrising.entity.PostStatus;

public record PostsDTO(
        Long id,
        String title,
        String content,
        PostStatus status
) {
}
