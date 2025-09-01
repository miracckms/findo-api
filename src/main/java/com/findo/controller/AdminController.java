package com.findo.controller;

import com.findo.dto.request.CategoryCreateRequest;
import com.findo.dto.request.CategoryUpdateRequest;
import com.findo.dto.response.AdResponse;
import com.findo.dto.response.CategoryResponse;
import com.findo.model.Ad;
import com.findo.model.enums.AdStatus;
import com.findo.repository.AdRepository;
import com.findo.repository.CategoryRepository;
import com.findo.service.AdService;
import com.findo.service.CategoryService;
import com.findo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.findo.model.Category;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/ads/pending")
    public ResponseEntity<Page<Ad>> getPendingAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ad> ads = adRepository.findPendingAds(pageable);
        return ResponseEntity.ok(ads);
    }

    @PostMapping("/ads/{id}/approve")
    public ResponseEntity<?> approveAd(@PathVariable String id) {
        try {
            Optional<Ad> adOptional = adRepository.findById(id);
            if (adOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ad ad = adOptional.get();
            if (ad.getStatus() != AdStatus.PENDING_APPROVAL) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid status");
                error.put("message", "Ad is not pending approval");
                return ResponseEntity.badRequest().body(error);
            }

            ad.setStatus(AdStatus.ACTIVE);
            ad.setPublishedAt(LocalDateTime.now());
            ad.setExpiresAt(LocalDateTime.now().plusDays(30)); // 30 days expiry
            adRepository.save(ad);

            // Send notification email
            if (ad.getUser().getEmail() != null && ad.getUser().getEmailVerified()) {
                emailService.sendAdApprovedEmail(ad.getUser().getEmail(), ad.getTitle());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad approved successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to approve ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/ads/{id}/reject")
    public ResponseEntity<?> rejectAd(@PathVariable String id, @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            if (reason == null || reason.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Rejection reason is required");
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Ad> adOptional = adRepository.findById(id);
            if (adOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ad ad = adOptional.get();
            if (ad.getStatus() != AdStatus.PENDING_APPROVAL) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid status");
                error.put("message", "Ad is not pending approval");
                return ResponseEntity.badRequest().body(error);
            }

            ad.setStatus(AdStatus.REJECTED);
            ad.setRejectionReason(reason);
            adRepository.save(ad);

            // Send notification email
            if (ad.getUser().getEmail() != null && ad.getUser().getEmailVerified()) {
                emailService.sendAdRejectedEmail(ad.getUser().getEmail(), ad.getTitle(), reason);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad rejected successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to reject ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalAds", adRepository.count());
        stats.put("activeAds", adRepository.countByStatus(AdStatus.ACTIVE));
        stats.put("pendingAds", adRepository.countByStatus(AdStatus.PENDING_APPROVAL));
        stats.put("rejectedAds", adRepository.countByStatus(AdStatus.REJECTED));

        return ResponseEntity.ok(stats);
    }

    // ==================== CATEGORY MANAGEMENT ====================

    /**
     * Sadece ana kategorileri listele (sayfalama ile) - Sub kategoriler dahil değil
     */
    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryResponse>> getRootCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sortOrder") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<CategoryResponse> categories = categoryService.getRootCategoriesForAdmin(pageable);
        return ResponseEntity.ok(categories);
    }

    /**
     * Sadece ana kategorileri listele (sayfalama olmadan) - Sub kategoriler dahil değil
     */
    @GetMapping("/categories/all")
    public ResponseEntity<List<CategoryResponse>> getAllRootCategoriesWithoutPaging() {
        List<CategoryResponse> categories = categoryService.getRootCategoriesForAdmin();
        return ResponseEntity.ok(categories);
    }

    /**
     * Tüm kategorileri listele (ana + alt kategoriler) - sayfalama ile
     */
    @GetMapping("/categories/complete")
    public ResponseEntity<Page<CategoryResponse>> getAllCategoriesComplete(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sortOrder") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<CategoryResponse> categories = categoryService.getAllCategoriesForAdmin(pageable);
        return ResponseEntity.ok(categories);
    }

    /**
     * Tüm kategorileri listele (ana + alt kategoriler) - sayfalama olmadan
     */
    @GetMapping("/categories/complete/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesCompleteWithoutPaging() {
        List<CategoryResponse> categories = categoryService.getAllCategoriesForAdmin();
        return ResponseEntity.ok(categories);
    }

    /**
     * Yeni kategori oluştur
     */
    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        try {
            CategoryResponse category = categoryService.createCategory(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category created successfully");
            response.put("category", category);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Kategori güncelle
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        try {
            CategoryResponse category = categoryService.updateCategory(id, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category updated successfully");
            response.put("category", category);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Kategoriyi sil (hard delete)
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable String id) {
        try {
            categoryService.deleteCategory(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category deleted successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Kategoriyi deaktive et (soft delete)
     */
    @PutMapping("/categories/{id}/deactivate")
    public ResponseEntity<?> deactivateCategory(@PathVariable String id) {
        try {
            categoryService.deactivateCategory(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category deactivated successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to deactivate category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Kategoriyi aktive et
     */
    @PutMapping("/categories/{id}/activate")
    public ResponseEntity<?> activateCategory(@PathVariable String id) {
        try {
            categoryService.activateCategory(id);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Category activated successfully");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to activate category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Kategori detaylarını getir
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable String id) {
        try {
            return categoryRepository.findById(id)
                    .map(category -> {
                        CategoryResponse response = categoryService.convertToCategoryResponse(category);
                        return ResponseEntity.ok(response);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get category");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // ==================== SUBCATEGORY MANAGEMENT ====================

    /**
     * Belirli bir üst kategorinin alt kategorilerini listele (sayfalı)
     */
    @GetMapping("/categories/{parentId}/subcategories")
    public ResponseEntity<?> getSubcategories(
            @PathVariable String parentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sortOrder") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            // Parent category'nin varlığını kontrol et
            Optional<Category> parent = categoryRepository.findById(parentId);
            if (parent.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Parent category not found");
                errorResponse.put("message", "Parent category not found with id: " + parentId);
                return ResponseEntity.notFound().build();
            }

            Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

            Page<Category> subcategories = categoryRepository.findByParentId(parentId, pageable);
            Page<CategoryResponse> subcategoryResponses = subcategories.map(categoryService::convertToCategoryResponse);

            return ResponseEntity.ok(subcategoryResponses);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get subcategories");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Belirli bir üst kategorinin tüm alt kategorilerini listele (sayfalama
     * olmadan)
     */
    @GetMapping("/categories/{parentId}/subcategories/all")
    public ResponseEntity<?> getAllSubcategories(@PathVariable String parentId) {
        try {
            // Parent category'nin varlığını kontrol et
            Optional<Category> parent = categoryRepository.findById(parentId);
            if (parent.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Parent category not found");
                errorResponse.put("message", "Parent category not found with id: " + parentId);
                return ResponseEntity.notFound().build();
            }

            List<Category> subcategories = categoryRepository.findByParentId(parentId);
            List<CategoryResponse> subcategoryResponses = subcategories.stream()
                    .map(categoryService::convertToCategoryResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(subcategoryResponses);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get subcategories");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Belirli bir üst kategoriye yeni alt kategori oluştur
     */
    @PostMapping("/categories/{parentId}/subcategories")
    public ResponseEntity<?> createSubcategory(
            @PathVariable String parentId,
            @Valid @RequestBody CategoryCreateRequest request) {
        try {
            // Parent category'nin varlığını kontrol et
            Optional<Category> parent = categoryRepository.findById(parentId);
            if (parent.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Parent category not found");
                errorResponse.put("message", "Parent category not found with id: " + parentId);
                return ResponseEntity.notFound().build();
            }

            // Parent ID'yi request'e set et
            request.setParentId(parentId);

            CategoryResponse subcategory = categoryService.createCategory(request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory created successfully");
            response.put("subcategory", subcategory);
            response.put("parentId", parentId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Alt kategoriyi güncelle
     */
    @PutMapping("/categories/{parentId}/subcategories/{subcategoryId}")
    public ResponseEntity<?> updateSubcategory(
            @PathVariable String parentId,
            @PathVariable String subcategoryId,
            @Valid @RequestBody CategoryUpdateRequest request) {
        try {
            // Alt kategorinin varlığını ve doğru parent'a ait olduğunu kontrol et
            Optional<Category> subcategoryOpt = categoryRepository.findById(subcategoryId);
            if (subcategoryOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Subcategory not found");
                errorResponse.put("message", "Subcategory not found with id: " + subcategoryId);
                return ResponseEntity.notFound().build();
            }

            Category subcategory = subcategoryOpt.get();
            if (subcategory.getParent() == null || !subcategory.getParent().getId().equals(parentId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid relationship");
                errorResponse.put("message", "Subcategory does not belong to the specified parent category");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Parent ID'yi sabit tut (bu endpoint ile parent değiştirilemez)
            request.setParentId(parentId);

            CategoryResponse updatedSubcategory = categoryService.updateCategory(subcategoryId, request);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory updated successfully");
            response.put("subcategory", updatedSubcategory);
            response.put("parentId", parentId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Alt kategoriyi sil
     */
    @DeleteMapping("/categories/{parentId}/subcategories/{subcategoryId}")
    public ResponseEntity<?> deleteSubcategory(
            @PathVariable String parentId,
            @PathVariable String subcategoryId) {
        try {
            // Alt kategorinin varlığını ve doğru parent'a ait olduğunu kontrol et
            Optional<Category> subcategoryOpt = categoryRepository.findById(subcategoryId);
            if (subcategoryOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Subcategory not found");
                errorResponse.put("message", "Subcategory not found with id: " + subcategoryId);
                return ResponseEntity.notFound().build();
            }

            Category subcategory = subcategoryOpt.get();
            if (subcategory.getParent() == null || !subcategory.getParent().getId().equals(parentId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid relationship");
                errorResponse.put("message", "Subcategory does not belong to the specified parent category");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            categoryService.deleteCategory(subcategoryId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory deleted successfully");
            response.put("subcategoryId", subcategoryId);
            response.put("parentId", parentId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Alt kategoriyi deaktive et
     */
    @PutMapping("/categories/{parentId}/subcategories/{subcategoryId}/deactivate")
    public ResponseEntity<?> deactivateSubcategory(
            @PathVariable String parentId,
            @PathVariable String subcategoryId) {
        try {
            // Alt kategorinin varlığını ve doğru parent'a ait olduğunu kontrol et
            Optional<Category> subcategoryOpt = categoryRepository.findById(subcategoryId);
            if (subcategoryOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Subcategory not found");
                errorResponse.put("message", "Subcategory not found with id: " + subcategoryId);
                return ResponseEntity.notFound().build();
            }

            Category subcategory = subcategoryOpt.get();
            if (subcategory.getParent() == null || !subcategory.getParent().getId().equals(parentId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid relationship");
                errorResponse.put("message", "Subcategory does not belong to the specified parent category");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            categoryService.deactivateCategory(subcategoryId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory deactivated successfully");
            response.put("subcategoryId", subcategoryId);
            response.put("parentId", parentId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to deactivate subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Alt kategoriyi aktive et
     */
    @PutMapping("/categories/{parentId}/subcategories/{subcategoryId}/activate")
    public ResponseEntity<?> activateSubcategory(
            @PathVariable String parentId,
            @PathVariable String subcategoryId) {
        try {
            // Alt kategorinin varlığını ve doğru parent'a ait olduğunu kontrol et
            Optional<Category> subcategoryOpt = categoryRepository.findById(subcategoryId);
            if (subcategoryOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Subcategory not found");
                errorResponse.put("message", "Subcategory not found with id: " + subcategoryId);
                return ResponseEntity.notFound().build();
            }

            Category subcategory = subcategoryOpt.get();
            if (subcategory.getParent() == null || !subcategory.getParent().getId().equals(parentId)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid relationship");
                errorResponse.put("message", "Subcategory does not belong to the specified parent category");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            categoryService.activateCategory(subcategoryId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory activated successfully");
            response.put("subcategoryId", subcategoryId);
            response.put("parentId", parentId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to activate subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Alt kategoriyi başka bir parent kategoriye taşı
     */
    @PutMapping("/subcategories/{subcategoryId}/move-to/{newParentId}")
    public ResponseEntity<?> moveSubcategory(
            @PathVariable String subcategoryId,
            @PathVariable String newParentId) {
        try {
            // Alt kategorinin varlığını kontrol et
            Optional<Category> subcategoryOpt = categoryRepository.findById(subcategoryId);
            if (subcategoryOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Subcategory not found");
                errorResponse.put("message", "Subcategory not found with id: " + subcategoryId);
                return ResponseEntity.notFound().build();
            }

            // Yeni parent'ın varlığını kontrol et
            Optional<Category> newParentOpt = categoryRepository.findById(newParentId);
            if (newParentOpt.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "New parent category not found");
                errorResponse.put("message", "New parent category not found with id: " + newParentId);
                return ResponseEntity.notFound().build();
            }

            Category subcategory = subcategoryOpt.get();

            // Update request oluştur
            CategoryUpdateRequest updateRequest = new CategoryUpdateRequest();
            updateRequest.setName(subcategory.getName());
            updateRequest.setDescription(subcategory.getDescription());
            updateRequest.setIcon(subcategory.getIcon());
            updateRequest.setSortOrder(subcategory.getSortOrder());
            updateRequest.setActive(subcategory.getActive());
            updateRequest.setParentId(newParentId);

            CategoryResponse updatedSubcategory = categoryService.updateCategory(subcategoryId, updateRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Subcategory moved successfully");
            response.put("subcategory", updatedSubcategory);
            response.put("oldParentId", subcategory.getParent() != null ? subcategory.getParent().getId() : null);
            response.put("newParentId", newParentId);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to move subcategory");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Ana kategorileri (parent'ı olmayan) listele - alt kategori oluştururken kullanmak için
     * Not: Bu endpoint artık /categories/all ile aynı. Geriye uyumluluk için korunuyor.
     */
    @GetMapping("/categories/root")
    public ResponseEntity<?> getRootCategoriesLegacy() {
        try {
            List<CategoryResponse> rootCategoryResponses = categoryService.getRootCategoriesForAdmin();
            return ResponseEntity.ok(rootCategoryResponses);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get root categories");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
