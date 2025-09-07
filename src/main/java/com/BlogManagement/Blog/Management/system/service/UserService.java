package com.BlogManagement.Blog.Management.system.service;
// src/main/java/com/example/blogmanagement/service/UserService.java
import com.BlogManagement.Blog.Management.system.dto.SignUpRequest;
import com.BlogManagement.Blog.Management.system.dto.UserSummary;
import com.BlogManagement.Blog.Management.system.entity.Role;
import com.BlogManagement.Blog.Management.system.entity.User;
import com.BlogManagement.Blog.Management.system.exception.BadRequestException;
import com.BlogManagement.Blog.Management.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address is already in use!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserSummary getCurrentUser(User currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getEmail());
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("User not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}