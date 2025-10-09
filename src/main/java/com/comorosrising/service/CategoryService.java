package com.comorosrising.service;

import com.comorosrising.entity.Category;
import com.comorosrising.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void createCategory(Category category){
        if(category == null){
            throw new IllegalArgumentException("Category must be provided");
        }
        String categoryName = category.getCategoryName();
        if(categoryName == null || categoryName.trim().isEmpty()){
            throw new IllegalArgumentException("Category name is required");
        }

        String trimmedName = categoryName.trim();
        if(categoryRepository.existsByCategoryName(trimmedName)){
            throw new IllegalArgumentException("Category name '" + trimmedName + "' already exists");
        }
        category.setCategoryName(trimmedName);
        categoryRepository.save(category);
    }

    public boolean updateCategory(Long categoryId, Category category){
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(categoryOptional.isPresent()){
            Category categoryToSave = categoryOptional.get();
            if(category.getCategoryName() != null && category.getCategoryName().trim().isEmpty()){
                if(categoryRepository.existsByCategoryName(category.getCategoryName().trim())){
                    throw new IllegalArgumentException("Category name '" + category.getCategoryName().trim() + "' already exists");
                }
            }
            categoryToSave.setCategoryName(category.getCategoryName());
            categoryRepository.save(categoryToSave);
            return true;
        }
        return false;
    }

    public boolean deleteCategory(Long categoryId){
        if(!categoryRepository.existsById(categoryId)){
            return false;
        }
        try{
            categoryRepository.deleteById(categoryId);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
