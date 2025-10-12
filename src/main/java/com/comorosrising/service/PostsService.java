package com.comorosrising.service;

import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.TagSearchDTO;
import com.comorosrising.entity.*;
import com.comorosrising.mapper.PostsMapper;
import com.comorosrising.repository.PostsRepository;
import com.comorosrising.repository.TagRepository;
import com.comorosrising.repository.UserRepository;
import com.comorosrising.utils.TagExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostsService {

    private static final Logger logger = LoggerFactory.getLogger(PostsService.class);

    private final PostsRepository postsRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TagRepository tagRepository;
    private final TagExtractor tagExtractor;
    private final PostsMapper postsMapper;

    public PostsService(PostsRepository postsRepository, UserService userService, CategoryService categoryService, TagRepository tagRepository, TagExtractor tagExtractor, PostsMapper postsMapper) {
        this.postsRepository = postsRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagRepository = tagRepository;
        this.tagExtractor = tagExtractor;
        this.postsMapper = postsMapper;
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
        Set<Tag> tags = new HashSet<>();
        if(postsDTO.content() != null && !postsDTO.content().isBlank()){
            Set<String> tagNames = tagExtractor.extractTags(postsDTO.content());
            for(String tagName : tagNames){
                Tag tag = tagRepository.findByTagName(tagName).orElseGet(() -> {
                    Tag newTag = new Tag();
                    newTag.setTagName(tagName);
                    return tagRepository.save(newTag);
                });
                tags.add(tag);
            }
        }
        Posts post = new Posts();
        post.setTitle(postsDTO.title());
        post.setContent(postsDTO.content());
        post.setStatus(postsDTO.status() != null ? postsDTO.status() : PostStatus.DRAFT);
        post.setUser(user);
        post.setCategory(category);
        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return postsRepository.save(post);
    }

    public boolean updatePosts(Long postId, PostsDTO postToUpdate){
        logger.info("Checking if user with id {} exist or not!", postToUpdate.userId());
        User user = userService.getUser(postToUpdate.userId());
        if(user == null){
            throw new IllegalArgumentException("Unauthorized action");
        }
        Optional<Posts> postsTOptional = postsRepository.findById(postId);

        if (postsTOptional.isPresent()){
            Posts postToSave = postsTOptional.get();
            postToSave.setTitle(postToUpdate.title());
            postToSave.setContent(postToUpdate.content());
            postToSave.setUpdatedAt(LocalDateTime.now());
            postToSave.setStatus(postToUpdate.status());
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

    //Searching post by tag sector

    // Search by single tag name
    public List<PostsDTO> searchPostsByTag(String tagName){
        if(tagName == null || tagName.trim().isEmpty()){
            throw new IllegalArgumentException("Tag name cannot be empty");
        }
        List<Posts> posts = postsRepository.findByTagName(tagName.trim());
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Advanced search with multiple options
    public List<PostsDTO> searchPostsByTags(TagSearchDTO searchDTO){
        if(searchDTO==null){
            throw new IllegalArgumentException("Search criteria cannot be null");
        }

        List<Posts> posts;

        switch (searchDTO.searchType()){
            case EXACT :
                if(searchDTO.tagName() == null){
                    throw new IllegalArgumentException("Tag name is required for EXACT search");
                }
                posts = postsRepository.findByTagName(searchDTO.tagName().trim());
                break;

            case ANY:
                if(searchDTO.tagNames() == null || searchDTO.tagNames().isEmpty()){
                    throw new IllegalArgumentException("Tag names are required for ANY search");
                }
                List<String> trimmedNames = searchDTO.tagNames().stream()
                        .map(String::trim).collect(Collectors.toList());
                posts = postsRepository.findByAnyTagNames(trimmedNames);
                break;

            case ALL:
                if(searchDTO.tagNames() == null || searchDTO.tagNames().isEmpty()){
                    throw new IllegalArgumentException("Tag names are required for ALL search");
                }
                List<String> trimmedNamesAll = searchDTO.tagNames().stream()
                        .map(String::trim).collect(Collectors.toList());
                posts = postsRepository.findByAllTagNames(trimmedNamesAll, trimmedNamesAll.size());
                break;

            case CONTAINS:
                if(searchDTO.tagName() == null){
                    throw new IllegalArgumentException("Tag name is required for CONTAINS search");
                }
                posts = postsRepository.findByTagNameContaining(searchDTO.tagName().trim());
                break;

            default:
                throw new IllegalArgumentException("Unsupported search type : " + searchDTO.searchType());
        }

        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Get related post by tag
    public List<PostsDTO> getRelatedPosts(Long postId, int limit){
        Posts post = postsRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("Post not found")
        );
        Set<Tag> postTags = post.getTags();
        if(postTags.isEmpty()){
            return new ArrayList<>();
        }

        List<String> tagNames = postTags.stream().map(Tag::getTagName).collect(Collectors.toList());

        List<Posts> relatedPosts = postsRepository.findByAnyTagNames(tagNames).stream()
                .filter(p -> !p.getId().equals(postId)) //exclude current post
                .limit(limit)
                .collect(Collectors.toList());

        return relatedPosts.stream().map(postsMapper::toDTO).collect(Collectors.toList());

    }
}
