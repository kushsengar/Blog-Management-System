package com.BlogManagement.Blog.Management.system.service;
// src/main/java/com/example/blogmanagement/service/PostService.java
import com.BlogManagement.Blog.Management.system.dto.PostRequest;
import com.BlogManagement.Blog.Management.system.dto.PostResponse;
import com.BlogManagement.Blog.Management.system.entity.Post;
import com.BlogManagement.Blog.Management.system.entity.User;
import com.BlogManagement.Blog.Management.system.exception.BadRequestException;
import com.BlogManagement.Blog.Management.system.exception.UnauthorizedException;
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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    public PostResponse createPost(PostRequest postRequest, UserPrincipal currentUser) {
        User user = userService.getUserById(currentUser.getId());

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(user);

        post = postRepository.save(post);
        return mapToPostResponse(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllPosts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(this::mapToPostResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found with id: " + id));
        return mapToPostResponse(post);
    }

    public PostResponse updatePost(Long id, PostRequest postRequest, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found with id: " + id));

        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("You can only edit your own posts");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        post = postRepository.save(post);
        return mapToPostResponse(post);
    }

    public void deletePost(Long id, UserPrincipal currentUser) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Post not found with id: " + id));

        if (!post.getAuthor().getId().equals(currentUser.getId()) &&
                !currentUser.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new UnauthorizedException("You don't have permission to delete this post");
        }

        postRepository.delete(post);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPostsByUser(Long userId, int page, int size) {
        User user = userService.getUserById(userId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Post> posts = postRepository.findByAuthor(user, pageable);
        return posts.map(this::mapToPostResponse);
    }

    @Transactional(readOnly = true)
    public boolean isOwner(Long postId, String username) {
        Post post = postRepository.findById(postId).orElse(null);
        return post != null && post.getAuthor().getUsername().equals(username);
    }

    private PostResponse mapToPostResponse(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setCreatedAt(post.getCreatedAt());
        postResponse.setUpdatedAt(post.getUpdatedAt());
        postResponse.setAuthorName(post.getAuthor().getUsername());
        postResponse.setCommentCount(post.getComments() != null ? post.getComments().size() : 0);
        return postResponse;
    }
}
