package com.findo.dto.response;

import java.util.List;
import java.util.UUID;

public class CityResponse {

    private UUID id;
    private String name;
    private String plateCode;
    private Boolean active;
    private List<DistrictResponse> districts;
    private Long adCount;

    // Constructors
    public CityResponse() {
    }

    public CityResponse(UUID id, String name, String plateCode) {
        this.id = id;
        this.name = name;
        this.plateCode = plateCode;
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

    public String getPlateCode() {
        return plateCode;
    }

    public void setPlateCode(String plateCode) {
        this.plateCode = plateCode;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<DistrictResponse> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DistrictResponse> districts) {
        this.districts = districts;
    }

    public Long getAdCount() {
        return adCount;
    }

    public void setAdCount(Long adCount) {
        this.adCount = adCount;
    }
}
