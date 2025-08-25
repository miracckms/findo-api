package com.findo.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${file.upload.max-size:10485760}") // 10MB
    private long maxFileSize;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final int THUMBNAIL_SIZE = 300;
    private static final int MAX_IMAGE_WIDTH = 1200;
    private static final int MAX_IMAGE_HEIGHT = 800;

    public String uploadImage(MultipartFile file) throws IOException {
        // Validate file
        validateFile(file);

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

        // Create subdirectories for organization
        String yearMonth = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM"));
        Path targetDir = uploadPath.resolve(yearMonth);
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        Path targetPath = targetDir.resolve(uniqueFilename);

        // Process and save image
        try {
            // Resize and compress main image
            Thumbnails.of(file.getInputStream())
                    .size(MAX_IMAGE_WIDTH, MAX_IMAGE_HEIGHT)
                    .outputQuality(0.8)
                    .toFile(targetPath.toFile());

            // Create thumbnail
            String thumbnailFilename = "thumb_" + uniqueFilename;
            Path thumbnailPath = targetDir.resolve(thumbnailFilename);

            Thumbnails.of(targetPath.toFile())
                    .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .outputQuality(0.7)
                    .toFile(thumbnailPath.toFile());

            // Return relative path for main image
            return yearMonth + "/" + uniqueFilename;

        } catch (IOException e) {
            // Clean up if processing failed
            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
            }
            throw new IOException("Failed to process image: " + e.getMessage());
        }
    }

    public String getThumbnailPath(String imagePath) {
        if (imagePath == null)
            return null;

        Path path = Paths.get(imagePath);
        String filename = path.getFileName().toString();
        String directory = path.getParent() != null ? path.getParent().toString() : "";

        return directory + (directory.isEmpty() ? "" : "/") + "thumb_" + filename;
    }

    public boolean deleteImage(String imagePath) {
        try {
            Path fullPath = Paths.get(uploadDir, imagePath);
            boolean mainDeleted = Files.deleteIfExists(fullPath);

            // Also delete thumbnail
            String thumbnailPath = getThumbnailPath(imagePath);
            if (thumbnailPath != null) {
                Path thumbnailFullPath = Paths.get(uploadDir, thumbnailPath);
                Files.deleteIfExists(thumbnailFullPath);
            }

            return mainDeleted;
        } catch (IOException e) {
            System.err.println("Failed to delete image: " + e.getMessage());
            return false;
        }
    }

    public String getFullImageUrl(String imagePath) {
        if (imagePath == null)
            return null;
        return "/uploads/" + imagePath;
    }

    public String getFullThumbnailUrl(String imagePath) {
        String thumbnailPath = getThumbnailPath(imagePath);
        if (thumbnailPath == null)
            return null;
        return "/uploads/" + thumbnailPath;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new IOException("File size exceeds maximum allowed size");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new IOException("Filename is invalid");
        }

        String extension = getFileExtension(filename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IOException("File type not allowed. Allowed types: " + String.join(", ", ALLOWED_EXTENSIONS));
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File is not a valid image");
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}
