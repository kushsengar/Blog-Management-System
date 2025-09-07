package com.BlogManagement.Blog.Management.system.service;
// src/test/java/com/example/blogmanagement/service/UserServiceTest.java
import com.BlogManagement.Blog.Management.system.dto.SignUpRequest;
import com.BlogManagement.Blog.Management.system.entity.Role;
import com.BlogManagement.Blog.Management.system.entity.User;
import com.BlogManagement.Blog.Management.system.exception.BadRequestException;
import com.BlogManagement.Blog.Management.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private SignUpRequest signUpRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password123");
    }

    @Test
    void createUser_Success() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(signUpRequest.getUsername());
        savedUser.setEmail(signUpRequest.getEmail());
        savedUser.setPassword("encodedPassword");
        savedUser.setRole(Role.USER);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        User result = userService.createUser(signUpRequest);

        // Then
        assertNotNull(result);
        assertEquals(signUpRequest.getUsername(), result.getUsername());
        assertEquals(signUpRequest.getEmail(), result.getEmail());
        assertEquals(Role.USER, result.getRole());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(signUpRequest.getPassword());
    }

    @Test
    void createUser_UsernameExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> userService.createUser(signUpRequest));

        assertEquals("Username is already taken!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_EmailExists_ThrowsException() {
        // Given
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> userService.createUser(signUpRequest));

        assertEquals("Email address is already in use!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
