package com.comorosrising.dto;

import java.util.List;

public record TagSearchDTO(
        String tagName,
        List<String> tagNames,
        SearchType searchType
) {
    public enum SearchType {
        EXACT, ANY, ALL, CONTAINS
    }
}
