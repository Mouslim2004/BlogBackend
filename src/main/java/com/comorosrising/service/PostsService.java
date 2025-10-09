package com.comorosrising.service;

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

    public PostsService(PostsRepository postsRepository, UserService userService) {
        this.postsRepository = postsRepository;
        this.userService = userService;
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

    public void createPosts(Posts posts){
        logger.info("Checking if user with id {} exist or not!", posts.getUser().getId());
        User user = userService.getUser(posts.getUser().getId());
        if(user == null){
            throw new IllegalArgumentException("User id must be provided");
        }
        if(posts.getTitle() == null || posts.getTitle().isBlank()){
            throw  new IllegalArgumentException("Title must be provided");
        }
        if(posts.getStatus() == null){
            posts.setStatus(PostStatus.DRAFT);
        }
        if(posts.getCreatedAt() == null && posts.getUpdatedAt() == null){
            posts.setCreatedAt(LocalDateTime.now());
            posts.setUpdatedAt(LocalDateTime.now());
        }
        postsRepository.save(posts);
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
