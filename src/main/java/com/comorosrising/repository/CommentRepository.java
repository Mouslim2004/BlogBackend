package com.comorosrising.repository;

import com.comorosrising.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdAndParentCommentIsNull(Long postId);

    List<Comment> findByParentCommentId(Long parentCommentId);

    List<Comment> findByUserId(Long userId);
}
