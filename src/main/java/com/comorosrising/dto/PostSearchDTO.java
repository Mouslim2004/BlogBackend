package com.comorosrising.dto;

public record PostSearchDTO(
        String keyword,
        Long categoryId,
        String categoryName
) {

}
