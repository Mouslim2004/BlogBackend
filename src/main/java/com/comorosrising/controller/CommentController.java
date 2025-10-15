package com.comorosrising.controller;

import com.comorosrising.dto.CommentDTO;
import com.comorosrising.dto.CommentResponseDTO;
import com.comorosrising.dto.UpdateCommentDTO;
import com.comorosrising.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody @Valid CommentDTO commentDTO){
        try{
            CommentResponseDTO commentResponseDTO = commentService.createComment(commentDTO);
            return ResponseEntity.ok(commentResponseDTO);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentByPost(@PathVariable Long postId){
        List<CommentResponseDTO> comments = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getComment(@PathVariable Long id){
        try{
            CommentResponseDTO commentResponseDTO = commentService.getComment(id);
            return ResponseEntity.ok(commentResponseDTO);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<CommentResponseDTO>> getReplies(@PathVariable Long id){
        List<CommentResponseDTO> replies = commentService.getReplies(id);
        return ResponseEntity.ok(replies);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentByUser(@PathVariable Long userId){
        List<CommentResponseDTO> comments = commentService.getCommentByUser(userId);
        return ResponseEntity.ok(comments);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCommentDTO updateCommentDTO
            ){
        try{
            CommentResponseDTO commentResponseDTO = commentService.updateComment(id, updateCommentDTO.content());
            return ResponseEntity.ok(commentResponseDTO);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        try{
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

}
