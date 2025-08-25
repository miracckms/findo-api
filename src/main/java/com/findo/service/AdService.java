package com.findo.service;

import com.findo.dto.request.AdCreateRequest;
import com.findo.dto.request.AdSearchRequest;
import com.findo.dto.response.*;
import com.findo.model.*;
import com.findo.model.enums.AdStatus;
import com.findo.model.enums.SortType;
import com.findo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private AdPhotoRepository adPhotoRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserService userService;

    public Page<AdResponse> searchAds(AdSearchRequest searchRequest, User currentUser) {
        Pageable pageable = createPageable(searchRequest);

        Page<Ad> ads = adRepository.searchAds(
                searchRequest.getCategoryId(),
                searchRequest.getCityId(),
                searchRequest.getDistrictId(),
                searchRequest.getMinPrice(),
                searchRequest.getMaxPrice(),
                searchRequest.getSearchTerm(),
                pageable);

        return ads.map(ad -> convertToAdResponse(ad, currentUser));
    }

    public Optional<AdResponse> getAdById(UUID id, User currentUser) {
        Optional<Ad> ad = adRepository.findById(id);

        if (ad.isPresent() && ad.get().getStatus() == AdStatus.ACTIVE) {
            // Increment view count
            ad.get().incrementViewCount();
            adRepository.save(ad.get());

            return Optional.of(convertToAdResponse(ad.get(), currentUser));
        }

        return Optional.empty();
    }

    public AdResponse createAd(AdCreateRequest request, User user) {
        // Validate category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Validate city
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        // Validate district
        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new RuntimeException("District not found"));

        // Check if user has reached the maximum number of ads
        long userAdCount = adRepository.countActiveAdsByUser(user);
        // This value should come from application properties
        int maxAdsPerUser = 50;

        if (userAdCount >= maxAdsPerUser) {
            throw new RuntimeException("Maximum number of ads reached");
        }

        Ad ad = new Ad();
        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setNegotiable(request.getNegotiable());
        ad.setFeatured(request.getFeatured());
        ad.setUrgent(request.getUrgent());
        ad.setUser(user);
        ad.setCategory(category);
        ad.setCity(city);
        ad.setDistrict(district);
        ad.setNeighborhood(request.getNeighborhood());
        ad.setContactPhone(request.getContactPhone() != null ? request.getContactPhone() : user.getPhone());
        ad.setStatus(AdStatus.DRAFT);

        Ad savedAd = adRepository.save(ad);

        // Handle photo associations
        if (request.getPhotoIds() != null && !request.getPhotoIds().isEmpty()) {
            for (UUID photoId : request.getPhotoIds()) {
                AdPhoto photo = adPhotoRepository.findById(photoId).orElse(null);
                if (photo != null && photo.getUploadedBy().getId().equals(user.getId())) {
                    photo.setAd(savedAd);
                    adPhotoRepository.save(photo);
                }
            }
        }

        return convertToAdResponse(savedAd, user);
    }

    public AdResponse updateAd(UUID id, AdCreateRequest request, User user) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this ad");
        }

        // Only allow updates for DRAFT or REJECTED ads
        if (ad.getStatus() != AdStatus.DRAFT && ad.getStatus() != AdStatus.REJECTED) {
            throw new RuntimeException("Ad cannot be updated in current status");
        }

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setNegotiable(request.getNegotiable());
        ad.setNeighborhood(request.getNeighborhood());
        ad.setContactPhone(request.getContactPhone() != null ? request.getContactPhone() : user.getPhone());

        Ad savedAd = adRepository.save(ad);
        return convertToAdResponse(savedAd, user);
    }

    public void submitAdForApproval(UUID id, User user) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to submit this ad");
        }

        if (ad.getStatus() != AdStatus.DRAFT) {
            throw new RuntimeException("Only draft ads can be submitted for approval");
        }

        ad.setStatus(AdStatus.PENDING_APPROVAL);
        adRepository.save(ad);
    }

    public void deleteAd(UUID id, User user) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        if (!ad.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this ad");
        }

        adRepository.delete(ad);
    }

    public Page<AdResponse> getUserAds(User user, Pageable pageable) {
        Page<Ad> ads = adRepository.findByUser(user, pageable);
        return ads.map(ad -> convertToAdResponse(ad, user));
    }

    public Page<AdResponse> getFeaturedAds(Pageable pageable, User currentUser) {
        Page<Ad> ads = adRepository.findFeaturedAds(pageable);
        return ads.map(ad -> convertToAdResponse(ad, currentUser));
    }

    private Pageable createPageable(AdSearchRequest searchRequest) {
        Sort sort;
        switch (searchRequest.getSortType()) {
            case OLDEST:
                sort = Sort.by(Sort.Direction.ASC, "createdAt");
                break;
            case PRICE_LOW_TO_HIGH:
                sort = Sort.by(Sort.Direction.ASC, "price");
                break;
            case PRICE_HIGH_TO_LOW:
                sort = Sort.by(Sort.Direction.DESC, "price");
                break;
            case NEWEST:
            default:
                sort = Sort.by(Sort.Direction.DESC, "createdAt");
                break;
        }

        return PageRequest.of(searchRequest.getPage(), searchRequest.getSize(), sort);
    }

    private AdResponse convertToAdResponse(Ad ad, User currentUser) {
        AdResponse response = new AdResponse();
        response.setId(ad.getId());
        response.setTitle(ad.getTitle());
        response.setDescription(ad.getDescription());
        response.setPrice(ad.getPrice());
        response.setNegotiable(ad.getNegotiable());
        response.setStatus(ad.getStatus());
        response.setViewCount(ad.getViewCount());
        response.setPublishedAt(ad.getPublishedAt());
        response.setExpiresAt(ad.getExpiresAt());
        response.setFeatured(ad.getFeatured());
        response.setUrgent(ad.getUrgent());
        response.setCreatedAt(ad.getCreatedAt());
        response.setUpdatedAt(ad.getUpdatedAt());

        // Set user info
        response.setUser(userService.convertToUserSummaryResponse(ad.getUser()));

        // Set category info
        response.setCategory(convertToCategoryResponse(ad.getCategory()));

        // Set location info
        response.setCity(new CityResponse(ad.getCity().getId(), ad.getCity().getName(), ad.getCity().getPlateCode()));
        response.setDistrict(new DistrictResponse(
                ad.getDistrict().getId(),
                ad.getDistrict().getName(),
                ad.getDistrict().getCity().getId(),
                ad.getDistrict().getCity().getName()));

        // Set photos
        List<AdPhotoResponse> photos = ad.getPhotos().stream()
                .map(this::convertToAdPhotoResponse)
                .collect(Collectors.toList());
        response.setPhotos(photos);

        // Set favorite info
        if (currentUser != null) {
            boolean isFavorite = favoriteRepository.existsByUserAndAd(currentUser, ad);
            response.setIsFavorite(isFavorite);
        }

        long favoriteCount = favoriteRepository.countByAd(ad);
        response.setFavoriteCount(favoriteCount);

        return response;
    }

    private CategoryResponse convertToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        response.setIcon(category.getIcon());
        response.setSortOrder(category.getSortOrder());
        response.setActive(category.getActive());

        if (category.getParent() != null) {
            CategoryResponse parent = new CategoryResponse();
            parent.setId(category.getParent().getId());
            parent.setName(category.getParent().getName());
            response.setParent(parent);
        }

        return response;
    }

    private AdPhotoResponse convertToAdPhotoResponse(AdPhoto photo) {
        return new AdPhotoResponse(
                photo.getId(),
                photo.getUrl(),
                photo.getThumbnailUrl(),
                photo.getSortOrder());
    }

    public Page<AdResponse> getAllPublishedAds(Pageable pageable, User currentUser) {
        Page<Ad> ads = adRepository.findByStatus(AdStatus.ACTIVE, pageable);
        return ads.map(ad -> convertToAdResponse(ad, currentUser));
    }
}
