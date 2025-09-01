package com.findo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

        @Value("${server.port:8080}")
        private String serverPort;

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Findo API")
                                                .version("1.0.0")
                                                .description("Findo - Classified Ads Platform API"))
                                .servers(List.of(
                                                new Server()
                                                                .url("https://findo-api.onrender.com/api")
                                                                .description("Production Server (Render)"),
                                                new Server()
                                                                .url("http://localhost:" + serverPort + "/api")
                                                                .description("Development Server")))
                                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                                .components(new Components()
                                                .addSecuritySchemes("Bearer Authentication",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")
                                                                                .description(
                                                                                                "JWT token'ı Authorization header'ında Bearer prefix'i ile gönderin.\n\n"
                                                                                                                +
                                                                                                                "Örnek: `Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`\n\n"
                                                                                                                +
                                                                                                                "Token almak için `/auth/login` endpoint'ini kullanın.")));
        }
}
