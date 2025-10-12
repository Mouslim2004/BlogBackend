package com.comorosrising.repository;

import com.comorosrising.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);

    List<Tag> findByTagNameContainingIgnoreCase(String tagName);

    List<Tag> findByTagNameIn(List<String> tagNames);
}
