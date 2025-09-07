package com.BlogManagement.Blog.Management.system.controller;
// src/main/java/com/example/blogmanagement/controller/UserController.java
import com.BlogManagement.Blog.Management.system.dto.UserSummary;
import com.BlogManagement.Blog.Management.system.security.CurrentUser;
import com.BlogManagement.Blog.Management.system.security.UserPrincipal;
import com.BlogManagement.Blog.Management.system.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "User management APIs")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get current user", description = "Get current authenticated user's profile")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = userService.getCurrentUser(
                userService.getUserById(currentUser.getId())
        );

        return ResponseEntity.ok(userSummary);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Get user profile by ID")
    public ResponseEntity<UserSummary> getUserById(@PathVariable Long id) {
        UserSummary userSummary = userService.getCurrentUser(
                userService.getUserById(id)
        );

        return ResponseEntity.ok(userSummary);
    }
}
