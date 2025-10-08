package com.comorosrising.repository.service;

import com.comorosrising.entity.Posts;
import com.comorosrising.entity.PostStatus;
import com.comorosrising.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostsService {

    private final PostsRepository postsRepository;

    public PostsService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
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
        try {
            postsRepository.deleteById(postId);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
