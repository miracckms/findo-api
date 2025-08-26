package com.findo.dto.response;

public class UserSummaryResponse {

    private String id;
    private String name; // Masked name for privacy
    private Boolean storeMode;
    private String storeName;

    // Constructors
    public UserSummaryResponse() {
    }

    public UserSummaryResponse(String id, String name, Boolean storeMode, String storeName) {
        this.id = id;
        this.name = name;
        this.storeMode = storeMode;
        this.storeName = storeName;
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

    public Boolean getStoreMode() {
        return storeMode;
    }

    public void setStoreMode(Boolean storeMode) {
        this.storeMode = storeMode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
