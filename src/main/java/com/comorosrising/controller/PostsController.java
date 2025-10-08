package com.comorosrising.controller;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.mapper.PostsMapper;
import com.comorosrising.repository.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostsService postsService;
    private final PostsMapper postsMapper;

    public PostsController(PostsService postsService, PostsMapper postsMapper) {
        this.postsService = postsService;
        this.postsMapper = postsMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostsDTO>> getPosts(){
        return ResponseEntity.ok(postsService.getAllPosts().stream().map(postsMapper::toDTO).toList());
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostsDTO postsDTO){
        postsService.createPosts(postsMapper.fromDTO(postsDTO));
        return ResponseEntity.ok("Post created successfully");
    }
}
