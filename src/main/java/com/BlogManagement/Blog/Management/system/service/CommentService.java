package com.BlogManagement.Blog.Management.system.service;
// src/main/java/com/example/blogmanagement/service/CommentService.java

import com.BlogManagement.Blog.Management.system.dto.CommentRequest;
import com.BlogManagement.Blog.Management.system.dto.CommentResponse;
import com.BlogManagement.Blog.Management.system.entity.Comment;
import com.BlogManagement.Blog.Management.system.entity.Post;
import com.BlogManagement.Blog.Management.system.entity.User;
import com.BlogManagement.Blog.Management.system.exception.BadRequestException;
import com.BlogManagement.Blog.Management.system.exception.UnauthorizedException;
import com.BlogManagement.Blog.Management.system.repository.CommentRepository;
import com.BlogManagement.Blog.Management.system.repository.PostRepository;
import com.BlogManagement.Blog.Management.system.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public CommentResponse createComment(Long postId, CommentRequest commentRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("Post not found with id: " + postId));

        User user = userService.getUserById(currentUser.getId());

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        comment.setAuthor(user);

        comment = commentRepository.save(comment);
        return mapToCommentResponse(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsByPost(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BadRequestException("Post not found with id: " + postId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Comment> comments = commentRepository.findByPost(post, pageable);

        return comments.map(this::mapToCommentResponse);
    }

    public CommentResponse updateComment(Long commentId, CommentRequest commentRequest, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Comment not found with id: " + commentId));

        if (!comment.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only edit your own comments");
        }

        comment.setContent(commentRequest.getContent());
        comment = commentRepository.save(comment);

        return mapToCommentResponse(comment);
    }

    public void deleteComment(Long commentId, UserPrincipal currentUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException("Comment not found with id: " + commentId));

        if (!comment.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("You don't have permission to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setCreatedAt(comment.getCreatedAt());
        commentResponse.setUpdatedAt(comment.getUpdatedAt());
        commentResponse.setAuthorName(comment.getAuthor().getUsername());
        return commentResponse;
    }
}
