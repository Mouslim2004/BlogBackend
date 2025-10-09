package com.comorosrising.controller;

import com.comorosrising.dto.CategoryDTO;
import com.comorosrising.entity.Category;
import com.comorosrising.mapper.CategoryMapper;
import com.comorosrising.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories().stream().map(categoryMapper::toDTO).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.createCategory(categoryMapper.fromDTO(categoryDTO));
        return ResponseEntity.ok("Category created");
    }

}
