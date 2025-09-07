package com.BlogManagement.Blog.Management.system.repository;
// src/main/java/com/example/blogmanagement/repository/PostRepository.java
import com.BlogManagement.Blog.Management.system.entity.Post;
import com.BlogManagement.Blog.Management.system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Find posts by author with pagination
    Page<Post> findByAuthor(User author, Pageable pageable);

    // Find posts by title containing text (case insensitive)
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Custom query to find posts ordered by creation date
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    Page<Post> findAllOrderByCreatedAtDesc(Pageable pageable);

    // Find posts by author ID
    List<Post> findByAuthorId(Long authorId);

    // Count posts by author
    long countByAuthor(User author);
}