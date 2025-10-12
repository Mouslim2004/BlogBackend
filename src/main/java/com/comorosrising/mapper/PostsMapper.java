package com.comorosrising.mapper;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.UserDTO;
import com.comorosrising.entity.Category;
import com.comorosrising.entity.Posts;
import com.comorosrising.entity.Tag;
import com.comorosrising.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostsMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public PostsMapper(UserMapper userMapper, CategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    public Posts fromDTO(PostsDTO postsDTO){

        Posts posts = new Posts();
        posts.setId(posts.getId());
        posts.setTitle(posts.getTitle());
        posts.setContent(posts.getContent());
        posts.setStatus(posts.getStatus());

        /*User user = new User();
        user.setId(postsDTO.id());
        posts.setUser(user);

        Category category = new Category();
        category.setId(postsDTO.categoryId());
        posts.setCategory(category);*/

        return posts;
    }

    public PostsDTO toDTO(Posts posts){
        List<String> tagNames = posts.getTags().stream().map(Tag::getTagName).collect(Collectors.toList());
        return  new PostsDTO(
                posts.getId(),
                posts.getTitle(),
                posts.getContent(),
                posts.getStatus(),
                tagNames,
                posts.getUser() != null ? posts.getUser().getId() : null,
                //posts.getUser() != null ? posts.getUser().getName() : null,
                posts.getCategory() != null ? posts.getCategory().getId() : null
                //posts.getCategory() != null ? posts.getCategory().getCategoryName() : null
                //userMapper.toOutputDTO(posts.getUser()),
                //categoryMapper.toDTO(posts.getCategory())
        );
    }
}
