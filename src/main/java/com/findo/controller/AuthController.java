package com.findo.controller;

import com.findo.dto.request.LoginRequest;
import com.findo.dto.request.ProfileUpdateRequest;
import com.findo.dto.request.RegisterRequest;
import com.findo.dto.response.AuthResponse;
import com.findo.model.User;
import com.findo.security.CustomUserDetailsService;
import com.findo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "Kullanıcı kimlik doğrulama ve yetkilendirme işlemleri")
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Kullanıcı Girişi", description = "E-posta/telefon ve şifre ile giriş yapın")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı giriş", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz giriş bilgileri")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Login failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Kullanıcı Kaydı", description = "Yeni kullanıcı hesabı oluşturun")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Başarılı kayıt", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Kayıt hatası")
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            AuthResponse authResponse = authService.register(registerRequest);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        try {
            boolean verified = authService.verifyEmail(token);

            Map<String, Object> response = new HashMap<>();
            if (verified) {
                response.put("success", true);
                response.put("message", "Email successfully verified");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid or expired verification token");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Email verification failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/verify-phone")
    public ResponseEntity<?> verifyPhone(@RequestParam String token) {
        try {
            boolean verified = authService.verifyPhone(token);

            Map<String, Object> response = new HashMap<>();
            if (verified) {
                response.put("success", true);
                response.put("message", "Phone successfully verified");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid or expired verification code");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Phone verification failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            authService.sendVerificationToken(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Verification tokens sent");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to resend verification");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @Operation(summary = "Mevcut Kullanıcı Bilgileri", description = "Giriş yapmış kullanıcının bilgilerini getirin")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kullanıcı bilgileri"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            // Authentication null kontrolü
            if (authentication == null || authentication.getPrincipal() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Not authenticated");
                error.put("message", "Kullanıcı giriş yapmamış");
                return ResponseEntity.status(401).body(error);
            }

            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("email", user.getEmail());
            response.put("phone", user.getPhone());
            response.put("role", user.getRole());
            response.put("status", user.getStatus());
            response.put("emailVerified", user.getEmailVerified());
            response.put("phoneVerified", user.getPhoneVerified());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get user info");
            error.put("message", e.getMessage());
            return ResponseEntity.status(401).body(error);
        }
    }

    @PutMapping("/profile")
    @Operation(summary = "Update user profile", description = "Update the authenticated user's profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Email or phone already exists")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody ProfileUpdateRequest request,
            Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Not authenticated");
                error.put("message", "Kullanıcı giriş yapmamış");
                return ResponseEntity.status(401).body(error);
            }

            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User currentUser = principal.getUser();

            User updatedUser = authService.updateUserProfile(currentUser, request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Profile updated successfully");
            response.put("user", authService.convertToUserResponse(updatedUser));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update profile");
            error.put("message", e.getMessage());
            return ResponseEntity.status(409).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update profile");
            error.put("message", "Internal server error");
            return ResponseEntity.status(500).body(error);
        }
    }
}
