package com.findo.dto.response;

import java.util.List;

public class CategoryResponse {

    private String id;
    private String name;
    private String description;
    private String icon;
    private Integer sortOrder;
    private Boolean active;
    private CategoryResponse parent;
    private List<CategoryResponse> children;
    private Long adCount;

    // Constructors
    public CategoryResponse() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public CategoryResponse getParent() {
        return parent;
    }

    public void setParent(CategoryResponse parent) {
        this.parent = parent;
    }

    public List<CategoryResponse> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryResponse> children) {
        this.children = children;
    }

    public Long getAdCount() {
        return adCount;
    }

    public void setAdCount(Long adCount) {
        this.adCount = adCount;
    }
}
