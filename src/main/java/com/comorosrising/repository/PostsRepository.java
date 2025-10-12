package com.comorosrising.repository;

import com.comorosrising.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    //Request for searching a post by tags

    //Search post by tag name
    @Query("SELECT p FROM Posts p JOIN p.tags t WHERE t.tagName = :tagName")
    List<Posts> findByTagName(@Param("tagName") String tagName);

    //Search post by multiple tags
    @Query("SELECT p FROM Posts p JOIN p.tags t WHERE t.tagName IN :tagNames GROUP BY p HAVING COUNT(DISTINCT t) = :tagCount")
    List<Posts> findByAllTagNames(@Param("tagNames") List<String> tagNames, @Param("tagCount") long tagCount);

    //Search post by any of the tags
    @Query("SELECT DISTINCT p FROM Posts p JOIN p.tags t WHERE t.tagName IN :tagNames")
    List<Posts> findByAnyTagNames(@Param("tagNames") List<String> tagNames);

    //Search posts containing tag name
    @Query("SELECT p FROM Posts p JOIN p.tags t WHERE LOWER(t.tagName) LIKE LOWER(CONCAT('%', :tagName ,'%'))")
    List<Posts> findByTagNameContaining(@Param("tagName") String tagName);

    //Request for searching post by title, content and category

    //Search by content
    @Query("SELECT p FROM Posts p WHERE LOWER(p.content) LIKE LOWER(CONCAT('%', :content ,'%'))")
    List<Posts> findByContentContaining(@Param("content") String content);

    //Search by title
    @Query("SELECT p FROM Posts p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title ,'%'))")
    List<Posts> findByTitleContaining(@Param("title") String title);

    //Search by both title and content
    @Query("SELECT p FROM Posts p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword ,'%'))")
    List<Posts> findByTitleOrContentContaining(@Param("keyword") String keyword);

    //Search by category ID
    List<Posts> findByCategoryId(Long categoryId);

    //Search by category name
    @Query("SELECT p FROM Posts p JOIN p.category c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :categoryName ,'%'))")
    List<Posts> findByCategoryNameContaining(@Param("categoryName") String categoryName);

    //Combined search : by category and content
    @Query("SELECT p FROM Posts p WHERE p.category.id = :categoryId AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword ,'%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword ,'%')))")
    List<Posts> findByCategoryAndKeyword(@Param("categoryId") Long categoryId, @Param("keyword") String keyword);

}
