package com.comorosrising.mapper;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.entity.Posts;
import org.springframework.stereotype.Component;

@Component
public class PostsMapper {

    public Posts fromDTO(PostsDTO postsDTO){
        return new Posts(
                postsDTO.id(),
                postsDTO.title(),
                postsDTO.content(),
                postsDTO.status(),
                null,
                null,
                null,
                null,
                null,
                null

        );
    }

    public PostsDTO toDTO(Posts posts){
        return  new PostsDTO(
            posts.getId(), posts.getTitle(), posts.getContent(), posts.getStatus()
        );
    }
}
