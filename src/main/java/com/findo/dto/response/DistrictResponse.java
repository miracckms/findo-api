package com.findo.dto.response;

public class DistrictResponse {

    private String id;
    private String name;
    private Boolean active;
    private String cityId;
    private String cityName;
    private Long adCount;

    // Constructors
    public DistrictResponse() {
    }

    public DistrictResponse(String id, String name, String cityId, String cityName) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
        this.cityName = cityName;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
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
