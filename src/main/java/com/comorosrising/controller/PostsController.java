package com.comorosrising.controller;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.TagSearchDTO;
import com.comorosrising.dto.UserDTO;
import com.comorosrising.entity.PostStatus;
import com.comorosrising.entity.Posts;
import com.comorosrising.entity.User;
import com.comorosrising.mapper.PostsMapper;
import com.comorosrising.mapper.UserMapper;
import com.comorosrising.repository.UserRepository;
import com.comorosrising.service.PostsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private static final Logger log = LoggerFactory.getLogger(PostsController.class);

    private final PostsService postsService;
    private final PostsMapper postsMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PostsController(PostsService postsService, PostsMapper postsMapper, UserRepository userRepository, UserMapper userMapper) {
        this.postsService = postsService;
        this.postsMapper = postsMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostsDTO>> getPosts(){
        return ResponseEntity.ok(postsService.getAllPosts().stream().map(postsMapper::toDTO).toList());
    }

    /*@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestPart("postsDTO") PostsDTO postsDTO,
            @RequestPart(value = "file", required = false) MultipartFile file
            ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        try {

            Posts post = postsService.createPosts(postsDTO, email, file);
            PostsDTO responseDTO = postsMapper.toDTO(post);
            return ResponseEntity.ok("Post created successfully");
        }catch(IllegalArgumentException | IOException e){
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        }
    }*/

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestPart("postsDTO") String postsDTOString,
            @RequestPart(value = "file", required = false) MultipartFile file
    ){

        System.out.println("=== CONTROLLER START ===");
        System.out.println("1. Entered createPost method");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("2. Got authentication: " + authentication);

        String email = authentication.getName();
        System.out.println("3. User email: " + email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        System.out.println("4. User found in DB: " + optionalUser.isPresent());

        if(optionalUser.isEmpty()){
            System.out.println("5. ERROR: User not found - returning 404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PostsDTO postsDTO = objectMapper.readValue(postsDTOString, PostsDTO.class);
            System.out.println("6. Calling postsService.createPosts()");
            Posts post = postsService.createPosts(postsDTO, email, file);
            System.out.println("7. Post created successfully with ID: " + post.getId());

            PostsDTO responseDTO = postsMapper.toDTO(post);
            System.out.println("8. Returning success response");
            return ResponseEntity.ok("Post created successfully");

        } catch(IllegalArgumentException | IOException e){
            System.out.println("9. ERROR caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            e.printStackTrace(); // This will show the full stack trace
            return ResponseEntity.badRequest().body("Error : " + e.getMessage());
        } finally {
            System.out.println("=== CONTROLLER END ===");
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostsDTO> getPost(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(postsMapper.toDTO(postsService.getSinglePost(postId)));
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(@PathVariable("postId") Long postId, @RequestBody PostsDTO postsDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDTO userDTO = userMapper.toDTO(optionalUser.get());
        boolean updatedPost = postsService.updatePosts(postId, postsDTO, userDTO.email());
        if(updatedPost){
            return ResponseEntity.ok(Map.of("message", "Post updated successfully"));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDTO userDTO = userMapper.toDTO(optionalUser.get());
        boolean deletedPost = postsService.deletePosts(postId, userDTO.email());
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
