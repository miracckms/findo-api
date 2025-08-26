package com.findo.service;

import com.findo.dto.request.CategoryCreateRequest;
import com.findo.dto.request.CategoryUpdateRequest;
import com.findo.dto.response.CategoryResponse;
import com.findo.model.Category;
import com.findo.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // Public methods for frontend
    public List<CategoryResponse> getRootCategories() {
        List<Category> categories = categoryRepository.findRootCategories();
        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> getCategoryChildren(String parentId) {
        List<Category> children = categoryRepository.findByParentIdAndActiveTrue(parentId);
        return children.stream()
                .map(this::convertToSimpleCategoryResponse)
                .collect(Collectors.toList());
    }

    public Optional<CategoryResponse> getCategoryById(String id) {
        return categoryRepository.findById(id)
                .filter(Category::getActive)
                .map(this::convertToCategoryResponse);
    }

    // Admin methods for CRUD operations
    public List<CategoryResponse> getAllCategoriesForAdmin() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
    }

    public Page<CategoryResponse> getAllCategoriesForAdmin(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(this::convertToCategoryResponse);
    }

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setSortOrder(request.getSortOrder());
        category.setActive(request.getActive());

        // Set parent if specified
        if (request.getParentId() != null && !request.getParentId().trim().isEmpty()) {
            Optional<Category> parent = categoryRepository.findById(request.getParentId());
            if (parent.isPresent()) {
                category.setParent(parent.get());
            } else {
                throw new RuntimeException("Parent category not found with id: " + request.getParentId());
            }
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());

        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        // Update parent if specified
        if (request.getParentId() != null) {
            if (request.getParentId().trim().isEmpty()) {
                category.setParent(null); // Make it a root category
            } else {
                // Check for circular reference
                if (request.getParentId().equals(id)) {
                    throw new RuntimeException("Category cannot be its own parent");
                }

                Optional<Category> parent = categoryRepository.findById(request.getParentId());
                if (parent.isPresent()) {
                    // Check if the new parent is not a descendant
                    if (isDescendant(category, parent.get())) {
                        throw new RuntimeException("Cannot set parent: circular reference detected");
                    }
                    category.setParent(parent.get());
                } else {
                    throw new RuntimeException("Parent category not found with id: " + request.getParentId());
                }
            }
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryResponse(savedCategory);
    }

    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        // Check if category has children
        List<Category> children = categoryRepository.findByParentIdAndActiveTrue(id);
        if (!children.isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete category with active subcategories. Please delete or move subcategories first.");
        }

        // Check if category has ads (you might want to handle this differently)
        if (category.getAds() != null && !category.getAds().isEmpty()) {
            throw new RuntimeException(
                    "Cannot delete category with existing ads. Please remove or reassign ads first.");
        }

        categoryRepository.delete(category);
    }

    public void deactivateCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        category.setActive(false);
        categoryRepository.save(category);

        // Optionally deactivate all children as well
        List<Category> children = categoryRepository.findByParentId(id);
        for (Category child : children) {
            child.setActive(false);
            categoryRepository.save(child);
        }
    }

    // Helper methods
    private boolean isDescendant(Category ancestor, Category potentialDescendant) {
        if (potentialDescendant == null) {
            return false;
        }

        Category current = potentialDescendant.getParent();
        while (current != null) {
            if (current.getId().equals(ancestor.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
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
        return response;
    }
}
