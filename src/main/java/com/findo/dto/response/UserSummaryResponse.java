package com.findo.dto.response;

import java.util.UUID;

public class UserSummaryResponse {

    private UUID id;
    private String name; // Masked name for privacy
    private Boolean storeMode;
    private String storeName;

    // Constructors
    public UserSummaryResponse() {
    }

    public UserSummaryResponse(UUID id, String name, Boolean storeMode, String storeName) {
        this.id = id;
        this.name = name;
        this.storeMode = storeMode;
        this.storeName = storeName;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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
