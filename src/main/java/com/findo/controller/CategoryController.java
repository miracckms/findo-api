package com.findo.controller;

import com.findo.dto.response.CategoryResponse;
import com.findo.model.Category;
import com.findo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

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
    public ResponseEntity<List<CategoryResponse>> getCategoryChildren(@PathVariable UUID id) {
        List<Category> children = categoryRepository.findByParentIdAndActiveTrue(id);
        List<CategoryResponse> response = children.stream()
                .map(this::convertToSimpleCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable UUID id) {
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
}
