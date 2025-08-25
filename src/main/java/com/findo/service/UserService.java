package com.findo.service;

import com.findo.dto.request.ProfileUpdateRequest;
import com.findo.dto.response.CityResponse;
import com.findo.dto.response.DistrictResponse;
import com.findo.dto.response.UserResponse;
import com.findo.dto.response.UserSummaryResponse;
import com.findo.model.City;
import com.findo.model.District;
import com.findo.model.User;
import com.findo.repository.CityRepository;
import com.findo.repository.DistrictRepository;
import com.findo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setEmailVerified(user.getEmailVerified());
        response.setPhoneVerified(user.getPhoneVerified());
        response.setNeighborhood(user.getNeighborhood());
        response.setStoreMode(user.getStoreMode());
        response.setStoreName(user.getStoreName());
        response.setStoreDescription(user.getStoreDescription());

        if (user.getCity() != null) {
            response.setCity(new CityResponse(
                    user.getCity().getId(),
                    user.getCity().getName(),
                    user.getCity().getPlateCode()));
        }

        if (user.getDistrict() != null) {
            response.setDistrict(new DistrictResponse(
                    user.getDistrict().getId(),
                    user.getDistrict().getName(),
                    user.getDistrict().getCity().getId(),
                    user.getDistrict().getCity().getName()));
        }

        return response;
    }

    public UserSummaryResponse convertToUserSummaryResponse(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getMaskedName(),
                user.getStoreMode(),
                user.getStoreName());
    }

    public User updateProfile(User user, ProfileUpdateRequest request) {
        // Update basic information
        if (request.getFirstName() != null && !request.getFirstName().trim().isEmpty()) {
            user.setFirstName(request.getFirstName().trim());
        }

        if (request.getLastName() != null && !request.getLastName().trim().isEmpty()) {
            user.setLastName(request.getLastName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            // Check if email is changing
            if (!request.getEmail().equals(user.getEmail())) {
                // Check if email already exists
                Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    throw new RuntimeException("Email already exists");
                }
                user.setEmail(request.getEmail().trim());
                user.setEmailVerified(false); // Reset email verification when email changes
            }
        }

        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            // Check if phone is changing
            if (!request.getPhone().equals(user.getPhone())) {
                // Check if phone already exists
                Optional<User> existingUser = userRepository.findByPhone(request.getPhone());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    throw new RuntimeException("Phone number already exists");
                }
                user.setPhone(request.getPhone().trim());
                user.setPhoneVerified(false); // Reset phone verification when phone changes
            }
        }

        if (request.getNeighborhood() != null) {
            user.setNeighborhood(request.getNeighborhood().trim());
        }

        // Update location
        if (request.getCityId() != null) {
            Optional<City> city = cityRepository.findById(request.getCityId());
            if (city.isPresent()) {
                user.setCity(city.get());
            } else {
                throw new RuntimeException("City not found");
            }
        }

        if (request.getDistrictId() != null) {
            Optional<District> district = districtRepository.findById(request.getDistrictId());
            if (district.isPresent()) {
                user.setDistrict(district.get());
            } else {
                throw new RuntimeException("District not found");
            }
        }

        // Update store information
        if (request.getStoreMode() != null) {
            user.setStoreMode(request.getStoreMode());
        }

        if (request.getStoreName() != null) {
            user.setStoreName(request.getStoreName().trim());
        }

        if (request.getStoreDescription() != null) {
            user.setStoreDescription(request.getStoreDescription().trim());
        }

        return userRepository.save(user);
    }
}
