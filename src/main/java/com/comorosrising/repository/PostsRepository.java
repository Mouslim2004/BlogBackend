package com.comorosrising.repository;

import com.comorosrising.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
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

}
