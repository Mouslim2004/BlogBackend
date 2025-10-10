package com.comorosrising.mapper;

import com.comorosrising.dto.CommentResponseDTO;
import com.comorosrising.entity.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public static CommentResponseDTO toDTO(Comment comment){
        if(comment ==  null){
            return null;
        }

        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getName() : null,
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                new ArrayList<>(),
                comment.getCreatedAt()
        );
    }

    public static CommentResponseDTO toDTOWithReplies(Comment comment){
        if(comment ==  null){
            return null;
        }
        List<CommentResponseDTO> replyDTO = comment.getReplies().stream()
                .map(CommentMapper::toDTOWithReplies)
                .collect(Collectors.toList());
        return new CommentResponseDTO(
                comment.getId(),
                comment.getContent(),
                comment.getPost() != null ? comment.getPost().getId() : null,
                comment.getUser() != null ? comment.getUser().getId() : null,
                comment.getUser() != null ? comment.getUser().getName() : null,
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                replyDTO,
                comment.getCreatedAt()
        );
    }

}
