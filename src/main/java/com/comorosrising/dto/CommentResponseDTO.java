package com.comorosrising.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponseDTO(
        Long id,
        String content,
        Long postId,
        Long userId,
        String username,
        Long parentCommentId,
        List<CommentResponseDTO> replies,
        LocalDateTime createdAt
) {
}
