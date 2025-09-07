package com.BlogManagement.Blog.Management.system.security;
// src/main/java/com/example/blogmanagement/security/CurrentUser.java
// src/main/java/com/example/blogmanagement/security/CurrentUser.java
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

/**
 * Custom annotation to inject the current authenticated user into controller methods.
 * This is a meta-annotation that combines @AuthenticationPrincipal with custom behavior.
 *
 * Usage:
 * @GetMapping("/me")
 * public ResponseEntity<UserSummary> getCurrentUser(@CurrentUser UserPrincipal currentUser) {
 *     // currentUser will be automatically injected with the authenticated user
 * }
 */
@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}