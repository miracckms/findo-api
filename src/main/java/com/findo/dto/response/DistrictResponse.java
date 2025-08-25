package com.findo.dto.response;

import java.util.UUID;

public class DistrictResponse {

    private UUID id;
    private String name;
    private Boolean active;
    private UUID cityId;
    private String cityName;
    private Long adCount;

    // Constructors
    public DistrictResponse() {
    }

    public DistrictResponse(UUID id, String name, UUID cityId, String cityName) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.cityName = cityName;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UUID getCityId() {
        return cityId;
    }

    public void setCityId(UUID cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Long getAdCount() {
        return adCount;
    }

    public void setAdCount(Long adCount) {
        this.adCount = adCount;
    }
}
