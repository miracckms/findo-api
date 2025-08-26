package com.findo.controller;

import com.findo.dto.response.CityResponse;
import com.findo.dto.response.DistrictResponse;
import com.findo.model.City;
import com.findo.model.District;
import com.findo.repository.CityRepository;
import com.findo.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @GetMapping("/cities")
    public ResponseEntity<List<CityResponse>> getAllCities() {
        List<City> cities = cityRepository.findAllActiveOrderByName();
        List<CityResponse> response = cities.stream()
                .map(this::convertToCityResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<CityResponse> getCity(@PathVariable String id) {
        return cityRepository.findById(id)
                .filter(City::getActive)
                .map(this::convertToCityResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cities/{id}/districts")
    public ResponseEntity<List<DistrictResponse>> getDistrictsByCity(@PathVariable String id) {
        List<District> districts = districtRepository.findByCityIdAndActiveTrue(id);
        List<DistrictResponse> response = districts.stream()
                .map(this::convertToDistrictResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/districts")
    public ResponseEntity<List<DistrictResponse>> getAllDistricts() {
        List<District> districts = districtRepository.findByActiveTrue();
        List<DistrictResponse> response = districts.stream()
                .map(this::convertToDistrictResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/districts/{id}")
    public ResponseEntity<DistrictResponse> getDistrict(@PathVariable String id) {
        return districtRepository.findById(id)
                .filter(District::getActive)
                .map(this::convertToDistrictResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private CityResponse convertToCityResponse(City city) {
        CityResponse response = new CityResponse();
        response.setId(city.getId());
        response.setName(city.getName());
        response.setPlateCode(city.getPlateCode());
        response.setActive(city.getActive());
        return response;
    }

    private DistrictResponse convertToDistrictResponse(District district) {
        DistrictResponse response = new DistrictResponse();
        response.setId(district.getId());
        response.setName(district.getName());
        response.setActive(district.getActive());
        response.setCityId(district.getCity().getId());
        response.setCityName(district.getCity().getName());
        return response;
    }
}
