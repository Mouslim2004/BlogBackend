package com.comorosrising.service;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.entity.Category;
import com.comorosrising.entity.Posts;
import com.comorosrising.entity.PostStatus;
import com.comorosrising.entity.User;
import com.comorosrising.repository.PostsRepository;
import com.comorosrising.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private static final Logger logger = LoggerFactory.getLogger(PostsService.class);

    private final PostsRepository postsRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    public PostsService(PostsRepository postsRepository, UserService userService, CategoryService categoryService) {
        this.postsRepository = postsRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public List<Posts> getAllPosts(){
        return postsRepository.findAll();
    }

    public Posts getSinglePost(Long postsId){
        if(postsId == null){
            throw new RuntimeException("Post id must be provided");
        }
        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(() -> new RuntimeException("Posts not found"));

        return posts;
    }

    public Posts createPosts(PostsDTO postsDTO){
        /*logger.info("Checking if user with id {} exist or not!", posts.getUser().getId());
        User user = userService.getUser(posts.getUser().getId());
        if(user == null){
            throw new IllegalArgumentException("User id must be provided");
        }
        Category category = categoryService.getCategory(posts.getCategory().getId());
        if(category == null){
            throw new IllegalArgumentException("Category must be assigned");
        }*/
        if (postsDTO.userId() == null) {
            throw new IllegalArgumentException("User ID must be provided");
        }
        if (postsDTO.categoryId() == null) {
            throw new IllegalArgumentException("Category ID must be provided");
        }

        logger.info("Checking if user with id {} exists", postsDTO.userId());
        User user = userService.getUser(postsDTO.userId());
        if(user == null) {
            throw new IllegalArgumentException("User not found with id: " + postsDTO.userId());
        }

        // Get category
        Category category = categoryService.getCategory(postsDTO.categoryId());
        if(category ==null) {
            throw new IllegalArgumentException("Category not found with id: " + postsDTO.categoryId());
        }

        if(postsDTO.title() == null || postsDTO.title().isBlank()){
            throw  new IllegalArgumentException("Title must be provided");
        }
        Posts post = new Posts();
        post.setTitle(postsDTO.title());
        post.setContent(postsDTO.content());
        post.setStatus(postsDTO.status() != null ? postsDTO.status() : PostStatus.DRAFT);
        post.setUser(user);
        post.setCategory(category);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return postsRepository.save(post);
    }

    public boolean updatePosts(Long postId, Posts postToUpdate){
        logger.info("Checking if user with id {} exist or not!", postToUpdate.getUser().getId());
        User user = userService.getUser(postToUpdate.getUser().getId());
        if(user == null){
            throw new IllegalArgumentException("Unauthorized action");
        }
        Optional<Posts> postsTOptional = postsRepository.findById(postId);

        if (postsTOptional.isPresent()){
            Posts postToSave = postsTOptional.get();
            postToSave.setTitle(postToUpdate.getTitle());
            postToSave.setContent(postToUpdate.getContent());
            postToSave.setUpdatedAt(LocalDateTime.now());
            postsRepository.save(postToSave);
            return true;
        }
        return false;
    }

    public boolean deletePosts(Long postId){
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        if(posts != null){
            User user = userService.getUser(posts.getUser().getId());
            if(user == null){
                throw new IllegalArgumentException("Unauthorized action");
            }
        }
        try {
            postsRepository.deleteById(postId);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
