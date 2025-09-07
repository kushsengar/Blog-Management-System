package com.BlogManagement.Blog.Management.system.dto;
// src/main/java/com/example/blogmanagement/dto/PostResponse.java
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Post response with all post details")
public class PostResponse {
    @Schema(description = "Post ID", example = "1")
    private Long id;

    @Schema(description = "Post title", example = "Introduction to Spring Boot")
    private String title;

    @Schema(description = "Post content", example = "This is a comprehensive guide to Spring Boot...")
    private String content;

    @Schema(description = "Author username", example = "john_doe")
    private String authorName;

    @Schema(description = "Post creation timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Post last update timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Number of comments", example = "5")
    private int commentCount;

    // Constructors
    public PostResponse() {}

    public PostResponse(Long id, String title, String content, String authorName,
                        LocalDateTime createdAt, LocalDateTime updatedAt, int commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = authorName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public int getCommentCount() { return commentCount; }
    public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
}
