package com.findo.dto.response;

public class AdPhotoResponse {

    private String id;
    private String url;
    private String thumbnailUrl;
    private Integer sortOrder;
    private String altText;

    // Constructors
    public AdPhotoResponse() {
    }

    public AdPhotoResponse(String id, String url, String thumbnailUrl, Integer sortOrder) {
        this.id = id;
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
        this.sortOrder = sortOrder;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }
}
