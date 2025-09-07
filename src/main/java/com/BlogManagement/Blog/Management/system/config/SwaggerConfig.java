package com.BlogManagement.Blog.Management.system.config;
// src/main/java/com/example/blogmanagement/config/SwaggerConfig.java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI blogManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blog Management API")
                        .description("A comprehensive blog management system with user authentication, post creation, and commenting features")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Developer")
                                .email("developer@example.com")
                                .url("https://github.com/example/blog-management")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")
                        ));
    }
}
