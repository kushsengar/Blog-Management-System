package com.BlogManagement.Blog.Management.system.controller;
// src/main/java/com/example/blogmanagement/controller/PostController.java
// src/main/java/com/example/blogmanagement/controller/PostController.java
import com.BlogManagement.Blog.Management.system.dto.ApiResponse;
import com.BlogManagement.Blog.Management.system.dto.PostRequest;
import com.BlogManagement.Blog.Management.system.dto.PostResponse;
import com.BlogManagement.Blog.Management.system.security.CurrentUser;
import com.BlogManagement.Blog.Management.system.security.UserPrincipal;
import com.BlogManagement.Blog.Management.system.service.PostService;
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
@RequestMapping("/api/posts")
@Tag(name = "Posts", description = "Blog post management APIs")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    @Operation(summary = "Get all posts", description = "Get paginated list of all blog posts")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Page<PostResponse> posts = postService.getAllPosts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID", description = "Get a specific blog post by its ID")
    public ResponseEntity<PostResponse> getPostById(
            @Parameter(description = "Post ID") @PathVariable Long id) {

        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create new post", description = "Create a new blog post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            @CurrentUser UserPrincipal currentUser) {

        PostResponse post = postService.createPost(postRequest, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update post", description = "Update an existing blog post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PostResponse> updatePost(
            @Parameter(description = "Post ID") @PathVariable Long id,
            @Valid @RequestBody PostRequest postRequest,
            @CurrentUser UserPrincipal currentUser) {

        PostResponse post = postService.updatePost(id, postRequest, currentUser);
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Delete post", description = "Delete a blog post")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse> deletePost(
            @Parameter(description = "Post ID") @PathVariable Long id,
            @CurrentUser UserPrincipal currentUser) {

        postService.deletePost(id, currentUser);
        return ResponseEntity.ok(new ApiResponse(true, "Post deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get posts by user", description = "Get paginated posts by specific user")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

        Page<PostResponse> posts = postService.getPostsByUser(userId, page, size);
        return ResponseEntity.ok(posts);
    }
}