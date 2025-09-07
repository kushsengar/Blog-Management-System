package com.BlogManagement.Blog.Management.system.dto;
// src/main/java/com/example/blogmanagement/security/CurrentUser.java
// src/main/java/com/example/blogmanagement/dto/ApiResponse.java
public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
