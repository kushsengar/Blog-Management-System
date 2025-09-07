package com.BlogManagement.Blog.Management.system.dto;
// src/main/java/com/example/blogmanagement/dto/CommentRequest.java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {
    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
    private String content;

    // Constructors
    public CommentRequest() {}

    public CommentRequest(String content) {
        this.content = content;
    }

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
