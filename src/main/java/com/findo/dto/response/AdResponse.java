package com.findo.dto.response;

import com.findo.model.enums.AdStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class AdResponse {

    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private Boolean negotiable;
    private AdStatus status;
    private Long viewCount;
    private LocalDateTime publishedAt;
    private LocalDateTime expiresAt;
    private Boolean featured;
    private Boolean urgent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relations
    private UserSummaryResponse user;
    private CategoryResponse category;
    private CityResponse city;
    private DistrictResponse district;
    private List<AdPhotoResponse> photos;

    // Additional fields
    private Boolean isFavorite = false;
    private Long favoriteCount = 0L;

    // Constructors
    public AdResponse() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getNegotiable() {
        return negotiable;
    }

    public void setNegotiable(Boolean negotiable) {
        this.negotiable = negotiable;
    }

    public AdStatus getStatus() {
        return status;
    }

    public void setStatus(AdStatus status) {
        this.status = status;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getUrgent() {
        return urgent;
    }

    public void setUrgent(Boolean urgent) {
        this.urgent = urgent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public void setUser(UserSummaryResponse user) {
        this.user = user;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public CityResponse getCity() {
        return city;
    }

    public void setCity(CityResponse city) {
        this.city = city;
    }

    public DistrictResponse getDistrict() {
        return district;
    }

    public void setDistrict(DistrictResponse district) {
        this.district = district;
    }

    public List<AdPhotoResponse> getPhotos() {
        return photos;
    }

    public void setPhotos(List<AdPhotoResponse> photos) {
        this.photos = photos;
    }

    public Boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
}
