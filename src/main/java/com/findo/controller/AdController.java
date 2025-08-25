package com.findo.controller;

import com.findo.dto.request.AdCreateRequest;
import com.findo.dto.request.AdSearchRequest;
import com.findo.dto.response.AdResponse;
import com.findo.model.User;
import com.findo.security.CustomUserDetailsService;
import com.findo.service.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "İlanlar", description = "İlan yönetimi ve arama işlemleri")
@RestController
@RequestMapping("/ads")
@CrossOrigin(origins = "*")
public class AdController {

    @Autowired
    private AdService adService;

    @Operation(summary = "İlan Arama", description = "Kategoriye, konuma, fiyata ve anahtar kelimeye göre ilan arayın")
    @ApiResponse(responseCode = "200", description = "Arama sonuçları")
    @GetMapping("/search")
    public ResponseEntity<Page<AdResponse>> searchAds(
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID cityId,
            @RequestParam(required = false) UUID districtId,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(defaultValue = "NEWEST") String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {

        AdSearchRequest searchRequest = new AdSearchRequest();
        searchRequest.setSearchTerm(searchTerm);
        searchRequest.setCategoryId(categoryId);
        searchRequest.setCityId(cityId);
        searchRequest.setDistrictId(districtId);

        if (minPrice != null && !minPrice.isEmpty()) {
            searchRequest.setMinPrice(new java.math.BigDecimal(minPrice));
        }
        if (maxPrice != null && !maxPrice.isEmpty()) {
            searchRequest.setMaxPrice(new java.math.BigDecimal(maxPrice));
        }

        try {
            searchRequest.setSortType(com.findo.model.enums.SortType.valueOf(sortType));
        } catch (IllegalArgumentException e) {
            searchRequest.setSortType(com.findo.model.enums.SortType.NEWEST);
        }

        searchRequest.setPage(page);
        searchRequest.setSize(size);

        User currentUser = null;
        if (authentication != null) {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            currentUser = principal.getUser();
        }

        Page<AdResponse> ads = adService.searchAds(searchRequest, currentUser);
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAd(@PathVariable UUID id, Authentication authentication) {
        User currentUser = null;
        if (authentication != null) {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            currentUser = principal.getUser();
        }

        Optional<AdResponse> ad = adService.getAdById(id, currentUser);
        if (ad.isPresent()) {
            return ResponseEntity.ok(ad.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ad not found");
            error.put("message", "Ad with ID " + id + " not found or not active");
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "İlan Oluştur", description = "Yeni ilan oluşturun")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "İlan başarıyla oluşturuldu", content = @Content(schema = @Schema(implementation = AdResponse.class))),
            @ApiResponse(responseCode = "400", description = "Geçersiz veri"),
            @ApiResponse(responseCode = "401", description = "Yetkisiz erişim")
    })
    @PostMapping
    public ResponseEntity<?> createAd(@Valid @RequestBody AdCreateRequest request,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            AdResponse ad = adService.createAd(request, user);
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable UUID id,
            @Valid @RequestBody AdCreateRequest request,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            AdResponse ad = adService.updateAd(id, request, user);
            return ResponseEntity.ok(ad);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitAdForApproval(@PathVariable UUID id,
            Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            adService.submitAdForApproval(id, user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad submitted for approval");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to submit ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable UUID id, Authentication authentication) {
        try {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            User user = principal.getUser();

            adService.deleteAd(id, user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/my-ads")
    public ResponseEntity<Page<AdResponse>> getMyAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {

        CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                .getPrincipal();
        User user = principal.getUser();

        Pageable pageable = PageRequest.of(page, size);
        Page<AdResponse> ads = adService.getUserAds(user, pageable);
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<AdResponse>> getFeaturedAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {

        User currentUser = null;
        if (authentication != null) {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            currentUser = principal.getUser();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<AdResponse> ads = adService.getFeaturedAds(pageable, currentUser);
        return ResponseEntity.ok(ads);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all ads", description = "Get all published ads with pagination. Public endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved ads"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters")
    })
    public ResponseEntity<Page<AdResponse>> getAllAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {

        User currentUser = null;
        if (authentication != null
                && authentication.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal) {
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) authentication
                    .getPrincipal();
            currentUser = principal.getUser();
        }

        // Create sort direction
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AdResponse> ads = adService.getAllPublishedAds(pageable, currentUser);
        return ResponseEntity.ok(ads);
    }
}
