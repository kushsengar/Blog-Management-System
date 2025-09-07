package com.BlogManagement.Blog.Management.system.config;
// src/main/java/com/example/blogmanagement/config/JpaConfig.java
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing  // Enables automatic timestamps (@CreatedDate, @LastModifiedDate)
public class JpaConfig {
    // This enables auditing features like automatic timestamps
}
