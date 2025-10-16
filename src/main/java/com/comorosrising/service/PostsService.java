package com.comorosrising.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.comorosrising.dto.PostsDTO;
import com.comorosrising.dto.TagSearchDTO;
import com.comorosrising.entity.*;
import com.comorosrising.mapper.PostsMapper;
import com.comorosrising.repository.CategoryRepository;
import com.comorosrising.repository.PostsRepository;
import com.comorosrising.repository.TagRepository;
import com.comorosrising.repository.UserRepository;
import com.comorosrising.utils.TagExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;

    public PostsService(PostsRepository postsRepository, UserService userService, CategoryService categoryService, TagRepository tagRepository, TagExtractor tagExtractor, PostsMapper postsMapper, CategoryRepository categoryRepository, Cloudinary cloudinary) {
        this.postsRepository = postsRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagRepository = tagRepository;
        this.tagExtractor = tagExtractor;
        this.postsMapper = postsMapper;
        this.categoryRepository = categoryRepository;
        this.cloudinary = cloudinary;
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

    /*public Posts createPosts(PostsDTO postsDTO, String email, MultipartFile file) throws IOException {

        logger.info("=== MULTIPART REQUEST RECEIVED ===");
        logger.info("Title: {}", postsDTO.title());
        logger.info("File: {}", file != null ? file.getOriginalFilename() + " (" + file.getContentType() + ")" : "NULL");
        User userEmail = userService.getUserByEmail(email);
        if(userEmail == null){
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        if (postsDTO.categoryId() == null) {
            throw new IllegalArgumentException("Category ID must be provided");
        }
        String fileUrl = null;
        if(file != null && !file.isEmpty()) {
            Map uploadFile = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );
            fileUrl = uploadFile.get("secure_url").toString();
        }

        logger.info("Checking if user with id {} exists", userEmail.getId());

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
        post.setUser(userEmail);
        post.setCategory(category);
        post.setImageUrl(fileUrl);
        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        return postsRepository.save(post);
    }*/

    public Posts createPosts(PostsDTO postsDTO, String email, MultipartFile file) throws IOException {

        System.out.println("=== SERVICE START ===");
        System.out.println("S1. Entered createPosts service method");
        System.out.println("S2. PostsDTO - title: " + postsDTO.title() + ", content: " + postsDTO.content());
        System.out.println("S3. Email: " + email);
        System.out.println("S4. File: " + (file != null ? file.getOriginalFilename() + " (size: " + file.getSize() + ")" : "NULL"));

        User userEmail = userService.getUserByEmail(email);
        System.out.println("S5. User from DB: " + (userEmail != null ? userEmail.getEmail() : "NULL"));

        if(userEmail == null){
            System.out.println("S6. ERROR: User not found");
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        if (postsDTO.categoryId() == null) {
            System.out.println("S7. ERROR: Category ID is null");
            throw new IllegalArgumentException("Category ID must be provided");
        }
        System.out.println("S8. Category ID: " + postsDTO.categoryId());

        String fileUrl = null;
        String publicId = null;
        if(file != null && !file.isEmpty()) {
            System.out.println("S9. Starting file upload to Cloudinary");
            try {
                Map uploadFile = cloudinary.uploader().upload(file.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto")
                );
                fileUrl = uploadFile.get("secure_url").toString();
                publicId = uploadFile.get("public_id").toString();
                System.out.println("S10. File uploaded successfully: " + fileUrl);
            } catch (Exception e) {
                System.out.println("S10. ERROR during file upload: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("S9. No file to upload");
        }

        System.out.println("S11. Checking if user with id " + userEmail.getId() + " exists");

        // Get category
        Category category = categoryService.getCategory(postsDTO.categoryId());
        System.out.println("S12. Category found: " + (category != null ? category.getCategoryName() : "NULL"));

        if(category ==null) {
            System.out.println("S13. ERROR: Category not found");
            throw new IllegalArgumentException("Category not found with id: " + postsDTO.categoryId());
        }

        if(postsDTO.title() == null || postsDTO.title().isBlank()){
            System.out.println("S14. ERROR: Title is blank");
            throw  new IllegalArgumentException("Title must be provided");
        }

        Set<Tag> tags = new HashSet<>();
        if(postsDTO.content() != null && !postsDTO.content().isBlank()){
            System.out.println("S15. Extracting tags from content");
            Set<String> tagNames = tagExtractor.extractTags(postsDTO.content());
            System.out.println("S16. Extracted tags: " + tagNames);

            for(String tagName : tagNames){
                Tag tag = tagRepository.findByTagName(tagName).orElseGet(() -> {
                    System.out.println("S17. Creating new tag: " + tagName);
                    Tag newTag = new Tag();
                    newTag.setTagName(tagName);
                    return tagRepository.save(newTag);
                });
                tags.add(tag);
            }
        } else {
            System.out.println("S15. No content for tag extraction");
        }

        System.out.println("S18. Creating Post entity");
        Posts post = new Posts();
        post.setTitle(postsDTO.title());
        post.setContent(postsDTO.content());
        post.setStatus(postsDTO.status() != null ? postsDTO.status() : PostStatus.DRAFT);
        post.setUser(userEmail);
        post.setCategory(category);
        post.setImageUrl(fileUrl);
        post.setPublicId(publicId);
        post.setTags(tags);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        System.out.println("S19. Saving post to database");
        Posts savedPost = postsRepository.save(post);
        System.out.println("S20. Post saved with ID: " + savedPost.getId());
        System.out.println("=== SERVICE END ===");

        return savedPost;
    }

    public boolean updatePosts(Long postId, PostsDTO postToUpdate, String email){
        User user = userService.getUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        logger.info("Checking if user with id {} exist or not!", user.getId());
        /*User user = userService.getUser(userEmail.getId());
        if(user == null){
            throw new IllegalArgumentException("Unauthorized action");
        }*/
        Optional<Posts> postsTOptional = postsRepository.findById(postId);

        if (postsTOptional.isPresent()){
            Posts postToSave = postsTOptional.get();
            if(postToSave.getUser().getId() != user.getId() && !user.getRole().name().equals(Role.ADMIN)){
                throw new IllegalArgumentException("Unauthorized action");
            }
            postToSave.setTitle(postToUpdate.title());
            postToSave.setContent(postToUpdate.content());
            postToSave.setUpdatedAt(LocalDateTime.now());
            postToSave.setStatus(postToUpdate.status());
            postsRepository.save(postToSave);
            return true;
        }
        return false;
    }

    public boolean deletePosts(Long postId, String email){
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User userEmail = userService.getUserByEmail(email);
        if(userEmail == null){
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        if(posts.getUser().getId() != userEmail.getId() && !userEmail.getRole().name().equals(Role.ADMIN)){
            throw new IllegalArgumentException("Unauthorized action");
        }
        if(posts != null){
            User user = userService.getUser(posts.getUser().getId());
            if(user == null){
                throw new IllegalArgumentException("Unauthorized action");
            }
        }
        try {
            cloudinary.uploader().destroy(posts.getPublicId(), Map.of());
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


    //Methods for looking for a post by title, category or content

    //Search a post by content
    public List<PostsDTO> searchByContent(String content){
        if(content == null || content.trim().isEmpty()){
            throw new IllegalArgumentException("Content cannot be empty");
        }
        List<Posts> posts = postsRepository.findByContentContaining(content.trim());
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Search post by title
    public List<PostsDTO> searchByTitle(String title){
        if(title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("Title cannot be empty");
        }
        List<Posts> posts = postsRepository.findByTitleContaining(title.trim());
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Search post by both title and content
    public List<PostsDTO> searchByKeyword(String keyword){
        if(keyword == null || keyword.trim().isEmpty()){
            throw new IllegalArgumentException("Keyword cannot be empty");
        }
        List<Posts> posts = postsRepository.findByTitleOrContentContaining(keyword.trim());
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Search by category ID
    public List<PostsDTO> searchByCategory(Long categoryId){
        if(categoryId == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        if(!categoryRepository.existsById(categoryId)){
            throw new IllegalArgumentException("Category not found with id : " + categoryId);
        }

        List<Posts> posts = postsRepository.findByCategoryId(categoryId);
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Search by category name
    public List<PostsDTO> searchByCategoryName(String categoryName){
        if(categoryName == null || categoryName.trim().isEmpty()){
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        List<Posts> posts = postsRepository.findByCategoryNameContaining(categoryName.trim());
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Search by category and keyword
    public List<PostsDTO> searchByCategoryAndKeyword(Long categoryId, String keyword){
        if(categoryId == null && !(keyword == null || keyword.trim().isEmpty())){
            throw new IllegalArgumentException("Either category id or keyword must be provided");
        }
        if(categoryId != null && !categoryRepository.existsById(categoryId)){
            throw new IllegalArgumentException("Category not found with id : " + categoryId);
        }
        List<Posts> posts = postsRepository.findByCategoryAndKeyword(categoryId, keyword != null ? keyword.trim() : "");
        return posts.stream().map(postsMapper::toDTO).collect(Collectors.toList());
    }

    //Get all categories with post count
    public Map<String, Long> getCategoriesWithPostCount(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().collect(
                Collectors.toMap(Category::getCategoryName, category -> (long) category.getPosts().size()
                        )
        );
    }

}
