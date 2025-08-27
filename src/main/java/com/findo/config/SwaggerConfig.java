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
                                                .description("Findo - Classified Ads Platform API Documentation\n\n" +
                                                                "Bu API, kullanıcıların ürünlerini satabilecekleri ve başka kullanıcıların "
                                                                +
                                                                "ürünleri görüntüleyip iletişime geçebilecekleri bir sınıflandırılmış ilan platformu sunar.\n\n"
                                                                +
                                                                "**Ana Özellikler:**\n" +
                                                                "- Kullanıcı kayıt/giriş sistemi (Email/SMS doğrulama)\n"
                                                                +
                                                                "- İlan oluşturma ve yönetimi\n" +
                                                                "- Gelişmiş arama ve filtreleme\n" +
                                                                "- Kategori, şehir, ilçe yönetimi\n" +
                                                                "- Görsel yükleme ve işleme\n" +
                                                                "- Favori sistemi\n" +
                                                                "- Admin moderasyon paneli\n\n" +
                                                                "**Test Hesapları:**\n" +
                                                                "- Admin: admin@findo.com / admin123\n" +
                                                                "- User: test@findo.com / test123")
                                                .contact(new Contact()
                                                                .name("Findo API Support")
                                                                .email("support@findo.com")
                                                                .url("https://findo.com"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")))
                                .servers(List.of(
                                                new Server()
                                                                .url("http://localhost:" + serverPort + "/api")
                                                                .description("Development Server"),
                                                new Server()
                                                                .url("https://findo-api.onrender.com/api")
                                                                .description("Production Server (Render)")))
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
