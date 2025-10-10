package com.comorosrising.dto;

public record CommentDTO(
        String content,
        Long postId,
        Long userId,
        Long parentCommentId
) {

}
