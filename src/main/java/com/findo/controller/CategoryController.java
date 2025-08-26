package com.findo.controller;

import com.findo.dto.request.CategoryCreateRequest;
import com.findo.dto.request.CategoryUpdateRequest;
import com.findo.dto.response.CategoryResponse;
import com.findo.model.Category;
import com.findo.repository.CategoryRepository;
import com.findo.service.CategoryService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getMainCategories() {
        List<Category> categories = categoryRepository.findRootCategories();
        List<CategoryResponse> response = categories.stream()
                .map(this::convertToSimpleCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryResponse>> getRootCategories() {
        List<Category> categories = categoryRepository.findRootCategories();
        List<CategoryResponse> response = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<List<CategoryResponse>> getCategoryChildren(@PathVariable String id) {
        List<Category> children = categoryRepository.findByParentIdAndActiveTrue(id);
        List<CategoryResponse> response = children.stream()
                .map(this::convertToSimpleCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable String id) {
        return categoryRepository.findById(id)
                .filter(Category::getActive)
                .map(this::convertToCategoryResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setIcon(category.getIcon());
        response.setSortOrder(category.getSortOrder());
        response.setActive(category.getActive());

        if (category.getParent() != null) {
            CategoryResponse parent = new CategoryResponse();
            parent.setId(category.getParent().getId());
            parent.setName(category.getParent().getName());
            response.setParent(parent);
        }

        // Convert children if needed (be careful with deep nesting)
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            List<CategoryResponse> children = category.getChildren().stream()
                    .filter(Category::getActive)
                    .map(child -> {
                        CategoryResponse childResponse = new CategoryResponse();
                        childResponse.setId(child.getId());
                        childResponse.setName(child.getName());
                        childResponse.setDescription(child.getDescription());
                        childResponse.setIcon(child.getIcon());
                        childResponse.setSortOrder(child.getSortOrder());
                        childResponse.setActive(child.getActive());
                        return childResponse;
                    })
                    .collect(Collectors.toList());
            response.setChildren(children);
        }

        return response;
    }

    private CategoryResponse convertToSimpleCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setIcon(category.getIcon());
        response.setSortOrder(category.getSortOrder());
        response.setActive(category.getActive());
        // Ana kategoriler için parent ve children bilgilerini dahil etmiyoruz
        return response;
    }

    // ==================== ADMIN ENDPOINTS ====================

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all")
    public ResponseEntity<List<CategoryResponse>> getAllCategoriesForAdmin() {
        List<CategoryResponse> categories = categoryService.getAllCategoriesForAdmin();
        return ResponseEntity.ok(categories);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<Page<CategoryResponse>> getAllCategoriesForAdminPaged(
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        try {
            CategoryResponse category = categoryService.createCategory(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody CategoryUpdateRequest request) {
        try {
            CategoryResponse category = categoryService.updateCategory(id, request);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable String id) {
        try {
            categoryService.deleteCategory(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Category deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}/deactivate")
    public ResponseEntity<Map<String, String>> deactivateCategory(@PathVariable String id) {
        try {
            categoryService.deactivateCategory(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Category deactivated successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
