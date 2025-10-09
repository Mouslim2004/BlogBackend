package com.comorosrising.mapper;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.UserDTO;
import com.comorosrising.entity.Posts;
import org.springframework.stereotype.Component;

@Component
public class PostsMapper {

    private final UserMapper userMapper;

    public PostsMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Posts fromDTO(PostsDTO postsDTO){
        return new Posts(
                postsDTO.id(),
                postsDTO.title(),
                postsDTO.content(),
                postsDTO.status(),
                null,
                null,
                userMapper.fromOutputDTO(postsDTO.user()),
                null,
                null,
                null

        );
    }

    public PostsDTO toDTO(Posts posts){
        return  new PostsDTO(
            posts.getId(), posts.getTitle(), posts.getContent(), posts.getStatus(), userMapper.toOutputDTO(posts.getUser())
        );
    }
}
