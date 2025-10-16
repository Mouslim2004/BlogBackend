package com.comorosrising.controller;

import com.comorosrising.dto.CategoryDTO;
import com.comorosrising.dto.UserDTO;
import com.comorosrising.entity.Category;
import com.comorosrising.entity.User;
import com.comorosrising.mapper.CategoryMapper;
import com.comorosrising.mapper.UserMapper;
import com.comorosrising.repository.UserRepository;
import com.comorosrising.service.CategoryService;
import com.comorosrising.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper, UserMapper userMapper, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(categoryMapper::toDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDTO userDTO = userMapper.toDTO(optionalUser.get());

        try{
            categoryService.createCategory(categoryMapper.fromDTO(categoryDTO));
            return ResponseEntity.ok("Category created");
        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }

    }

}
