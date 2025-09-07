package com.BlogManagement.Blog.Management.system.repository;
// src/main/java/com/example/blogmanagement/repository/UserRepository.java
import com.BlogManagement.Blog.Management.system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // Indicates this is a repository component
public interface UserRepository extends JpaRepository<User, Long> {

    // Method name query - Spring automatically generates SQL
    Optional<User> findByUsername(String username);

    // Method name query
    Optional<User> findByEmail(String email);

    // Check if username exists
    boolean existsByUsername(String username);

    // Check if email exists
    boolean existsByEmail(String email);

    // Custom JPQL query
    @Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
    Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
}
