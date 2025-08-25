package com.findo.controller;

import com.findo.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/uploads")
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping("/images")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            if (authentication == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Authentication required");
                error.put("message", "You must be logged in to upload images");
                return ResponseEntity.status(401).body(error);
            }

            String imagePath = fileUploadService.uploadImage(file);
            String imageUrl = fileUploadService.getFullImageUrl(imagePath);
            String thumbnailUrl = fileUploadService.getFullThumbnailUrl(imagePath);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Image uploaded successfully");
            response.put("imagePath", imagePath);
            response.put("imageUrl", imageUrl);
            response.put("thumbnailUrl", thumbnailUrl);
            response.put("originalFilename", file.getOriginalFilename());
            response.put("fileSize", file.getSize());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Upload failed");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Upload failed");
            error.put("message", "An unexpected error occurred");
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/**")
    public ResponseEntity<Resource> serveFile(@RequestParam String file) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(file).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                // Try to determine file's content type
                String contentType = null;
                try {
                    contentType = java.nio.file.Files.probeContentType(filePath);
                } catch (IOException ex) {
                    // Fallback to default content type
                }

                // Fallback to default content type if type could not be determined
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/images")
    public ResponseEntity<?> deleteImage(@RequestParam String imagePath,
            Authentication authentication) {
        try {
            if (authentication == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Authentication required");
                error.put("message", "You must be logged in to delete images");
                return ResponseEntity.status(401).body(error);
            }

            boolean deleted = fileUploadService.deleteImage(imagePath);

            Map<String, Object> response = new HashMap<>();
            if (deleted) {
                response.put("success", true);
                response.put("message", "Image deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Image not found or could not be deleted");
                return ResponseEntity.badRequest().body(response);
            }

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Delete failed");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
