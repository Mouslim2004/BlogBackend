package com.comorosrising.service;

import com.comorosrising.dto.CommentDTO;
import com.comorosrising.dto.CommentResponseDTO;
import com.comorosrising.entity.Comment;
import com.comorosrising.entity.Posts;
import com.comorosrising.entity.User;
import com.comorosrising.mapper.CommentMapper;
import com.comorosrising.repository.CommentRepository;
import com.comorosrising.repository.PostsRepository;
import com.comorosrising.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostsRepository postsRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postsRepository = postsRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDTO createComment(CommentDTO commentDTO, String email){
        if(commentDTO.content() == null || commentDTO.content().trim().isEmpty()){
            throw  new IllegalArgumentException("Comment content is required");
        }
        Posts posts = postsRepository.findById(commentDTO.postId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id : " + commentDTO.postId()));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email : " + email));
        Comment comment = new Comment();
        comment.setContent(commentDTO.content().trim());
        comment.setPost(posts);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        if(commentDTO.parentCommentId() != null){
            Comment parentComment = commentRepository.findById(commentDTO.parentCommentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found with it : " + commentDTO.parentCommentId()));
            comment.setParentComment(parentComment);
        }
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.toDTO(savedComment);
    }

    public List<CommentResponseDTO> getCommentsByPost(Long postId){
        List<Comment> comments = commentRepository.findByPostIdAndParentCommentIsNull(postId);
        return comments.stream().map(CommentMapper::toDTOWithReplies).collect(Collectors.toList());
    }

    public List<CommentResponseDTO> getReplies(Long parentCommentId){
        List<Comment> replies = commentRepository.findByParentCommentId(parentCommentId);
        return replies.stream().map(CommentMapper::toDTO).collect(Collectors.toList());
    }

    public CommentResponseDTO getComment(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id : " + id));
        return CommentMapper.toDTO(comment);
    }

    public CommentResponseDTO updateComment(Long id, String content, String email){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id : " + id));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email : " + email));
        if(content != null && !content.trim().isEmpty()){
            comment.setCreatedAt(LocalDateTime.now());
            comment.setContent(content.trim());

            Comment updatedComment = commentRepository.save(comment);
            return CommentMapper.toDTO(updatedComment);
        }

        throw new IllegalArgumentException("Content cannot be empty");
    }

    public void deleteComment(Long id, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email : " + email));
        if(!commentRepository.existsById(id)){
            throw new IllegalArgumentException("Comment not found with id : " + id);
        }
        commentRepository.deleteById(id);
    }

    public List<CommentResponseDTO> getCommentByUser(Long userId){
        List<Comment> comments = commentRepository.findByUserId(userId);
        return comments.stream().map(CommentMapper::toDTO).collect(Collectors.toList());
    }

}
