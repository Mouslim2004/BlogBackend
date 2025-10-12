package com.comorosrising.controller;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.TagSearchDTO;
import com.comorosrising.entity.Posts;
import com.comorosrising.mapper.PostsMapper;
import com.comorosrising.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        try {
            Posts post = postsService.createPosts(postsDTO);
            PostsDTO responseDTO = postsMapper.toDTO(post);
            return ResponseEntity.ok("Post created successfully");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostsDTO> getPost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postsMapper.toDTO(postsService.getSinglePost(postId)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId, @RequestBody PostsDTO postsDTO){
        boolean updatedPost = postsService.updatePosts(postId, postsDTO);
        if(updatedPost){
            return ResponseEntity.ok("Post updated successfully");
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId){
        boolean deletedPost = postsService.deletePosts(postId);
        if(deletedPost){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("Post not found", HttpStatus.NOT_FOUND);
    }

    //Post tag request

    //Search by single tag
    @GetMapping("/search/tag")
    public ResponseEntity<List<PostsDTO>> searchByTag(@RequestParam String tag){
        try{
            List<PostsDTO> posts = postsService.searchPostsByTag(tag);
            return ResponseEntity.ok(posts);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/search/tags")
    public ResponseEntity<List<PostsDTO>> searchByTags(@RequestBody TagSearchDTO searchDTO){
        try{
            List<PostsDTO> posts = postsService.searchPostsByTags(searchDTO);
            return ResponseEntity.ok(posts);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(new ArrayList<>());
        }
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<List<PostsDTO>> getRelatedPosts(@PathVariable Long id,
                                                          @RequestParam(defaultValue = "5") int limit){
        try{
            List<PostsDTO> relatedPosts = postsService.getRelatedPosts(id, limit);
            return ResponseEntity.ok(relatedPosts);
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    // Post keyword research

    //Search by content
    @GetMapping("/search/content")
    public ResponseEntity<List<PostsDTO>> searchByContent(@RequestParam String content){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByContent(content);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Search by title
    @GetMapping("/search/title")
    public ResponseEntity<List<PostsDTO>> searchByTitle(@RequestParam String title){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByTitle(title);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Search by keyword (title or content)
    @GetMapping("/search")
    public ResponseEntity<List<PostsDTO>> searchByKeyword(@RequestParam String keyword){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByKeyword(keyword);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Search by category ID
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<PostsDTO>> getPostsByCategory(@PathVariable Long categoryId){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByCategory(categoryId);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Search by category name
    @GetMapping("/category/name/{categoryName}")
    public ResponseEntity<List<PostsDTO>> getPostsByCategoryName(@PathVariable String categoryName){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByCategoryName(categoryName);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Combined search : category and keyword
    @GetMapping("/search/combined")
    public ResponseEntity<List<PostsDTO>> searchCombined(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword
    ){
        try{
            List<PostsDTO> postsDTOS = postsService.searchByCategoryAndKeyword(categoryId, keyword);
            return ResponseEntity.ok(postsDTOS);
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //Get categories with post count
    @GetMapping("/categories/counts")
    public ResponseEntity<Map<String, Long>> getCategoriesWithCount(){
        Map<String, Long> categoriesWithCount = postsService.getCategoriesWithPostCount();
        return ResponseEntity.ok(categoriesWithCount);
    }


}
