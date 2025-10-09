package com.comorosrising.mapper;

import com.comorosrising.dto.CategoryDTO;
import com.comorosrising.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category fromDTO(CategoryDTO categoryDTO){
        return new Category(
                categoryDTO.id(),
                categoryDTO.categoryName(),
                null
        );
    }

    public CategoryDTO toDTO(Category category){
        return new CategoryDTO(
                category.getId(), category.getCategoryName()
        );
    }
}
