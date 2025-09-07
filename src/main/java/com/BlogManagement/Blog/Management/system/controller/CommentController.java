package com.BlogManagement.Blog.Management.system.controller;
// src/main/java/com/example/blogmanagement/controller/CommentController.java
import com.BlogManagement.Blog.Management.system.dto.ApiResponse;
import com.BlogManagement.Blog.Management.system.dto.CommentRequest;
import com.BlogManagement.Blog.Management.system.dto.CommentResponse;
import com.BlogManagement.Blog.Management.system.security.CurrentUser;
import com.BlogManagement.Blog.Management.system.security.UserPrincipal;
import com.BlogManagement.Blog.Management.system.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comments", description = "Comment management APIs")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get comments by post", description = "Get paginated comments for a specific post")
    public ResponseEntity<Page<CommentResponse>> getCommentsByPost(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Page<CommentResponse> comments = commentService.getCommentsByPost(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create comment", description = "Create a new comment on a post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal currentUser) {

        CommentResponse comment = commentService.createComment(postId, commentRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update comment", description = "Update an existing comment")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CommentResponse> updateComment(
            @Parameter(description = "Comment ID") @PathVariable Long id,
            @Valid @RequestBody CommentRequest commentRequest,
            @CurrentUser UserPrincipal currentUser) {

        CommentResponse comment = commentService.updateComment(id, commentRequest, currentUser);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete comment", description = "Delete a comment")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse> deleteComment(
            @Parameter(description = "Comment ID") @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {

        commentService.deleteComment(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Comment deleted successfully"));
    }
}