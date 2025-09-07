package com.BlogManagement.Blog.Management.system.repository;
// src/main/java/com/example/blogmanagement/repository/CommentRepository.java
import com.BlogManagement.Blog.Management.system.entity.Comment;
import com.BlogManagement.Blog.Management.system.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Find comments by post with pagination
    Page<Comment> findByPost(Post post, Pageable pageable);

    // Find comments by post ID
    List<Comment> findByPostId(Long postId);

    // Find comments by author ID
    List<Comment> findByAuthorId(Long authorId);

    // Count comments by post
    long countByPost(Post post);
}
