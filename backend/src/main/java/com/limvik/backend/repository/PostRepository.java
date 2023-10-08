package com.limvik.backend.repository;

import com.limvik.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.positionName LIKE %:keyword% OR p.jobDescription LIKE %:keyword%")
    List<Post> search(@Param("keyword") String keyword);

}
