package com.findo.service;

import com.findo.dto.request.LoginRequest;
import com.findo.dto.request.ProfileUpdateRequest;
import com.findo.dto.request.RegisterRequest;
import com.findo.dto.response.AuthResponse;
import com.findo.dto.response.UserResponse;
import com.findo.model.City;
import com.findo.model.District;
import com.findo.model.User;
import com.findo.model.VerificationToken;
import com.findo.model.enums.UserStatus;
import com.findo.repository.CityRepository;
import com.findo.repository.DistrictRepository;
import com.findo.repository.UserRepository;
import com.findo.repository.VerificationTokenRepository;
import com.findo.security.CustomUserDetailsService;
import com.findo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    public AuthResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CustomUserDetailsService.CustomUserPrincipal principal = (CustomUserDetailsService.CustomUserPrincipal) userDetails;

            String token = jwtUtil.generateToken(userDetails);
            UserResponse userResponse = userService.convertToUserResponse(principal.getUser());

            return new AuthResponse(token, userResponse);

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        // Check if user already exists
        if (registerRequest.getEmail() != null && userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (registerRequest.getPhone() != null && userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Validate that at least email or phone is provided
        if (registerRequest.getEmail() == null && registerRequest.getPhone() == null) {
            throw new RuntimeException("Either email or phone number is required");
        }

        // Create new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setNeighborhood(registerRequest.getNeighborhood());
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        // Set city and district if provided
        if (registerRequest.getCityId() != null) {
            Optional<City> city = cityRepository.findById(registerRequest.getCityId());
            if (city.isPresent()) {
                user.setCity(city.get());
            }
        }

        if (registerRequest.getDistrictId() != null) {
            Optional<District> district = districtRepository.findById(registerRequest.getDistrictId());
            if (district.isPresent()) {
                user.setDistrict(district.get());
            }
        }

        User savedUser = userRepository.save(user);

        // Send verification token
        sendVerificationToken(savedUser);

        // Generate JWT token
        UserDetails userDetails = new CustomUserDetailsService.CustomUserPrincipal(savedUser);
        String token = jwtUtil.generateToken(userDetails);
        UserResponse userResponse = userService.convertToUserResponse(savedUser);

        return new AuthResponse(token, userResponse);
    }

    public void sendVerificationToken(User user) {
        // Generate verification token
        String token = generateVerificationToken();

        if (user.getEmail() != null && !user.getEmailVerified()) {
            VerificationToken emailToken = new VerificationToken(
                    token,
                    "EMAIL_VERIFICATION",
                    LocalDateTime.now().plusHours(24),
                    user);
            verificationTokenRepository.save(emailToken);
            emailService.sendVerificationEmail(user.getEmail(), token);
        }

        if (user.getPhone() != null && !user.getPhoneVerified()) {
            String smsToken = generateSmsToken();
            VerificationToken phoneToken = new VerificationToken(
                    smsToken,
                    "PHONE_VERIFICATION",
                    LocalDateTime.now().plusMinutes(10),
                    user);
            verificationTokenRepository.save(phoneToken);
            smsService.sendVerificationSms(user.getPhone(), smsToken);
        }
    }

    public boolean verifyEmail(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByTokenAndTokenType(token,
                "EMAIL_VERIFICATION");

        if (verificationToken.isEmpty() || !verificationToken.get().isValid()) {
            return false;
        }

        VerificationToken vToken = verificationToken.get();
        User user = vToken.getUser();
        user.setEmailVerified(true);

        // Check if user should be activated
        if (shouldActivateUser(user)) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
        vToken.markAsUsed();
        verificationTokenRepository.save(vToken);

        return true;
    }

    public boolean verifyPhone(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByTokenAndTokenType(token,
                "PHONE_VERIFICATION");

        if (verificationToken.isEmpty() || !verificationToken.get().isValid()) {
            return false;
        }

        VerificationToken vToken = verificationToken.get();
        User user = vToken.getUser();
        user.setPhoneVerified(true);

        // Check if user should be activated
        if (shouldActivateUser(user)) {
            user.setStatus(UserStatus.ACTIVE);
        }

        userRepository.save(user);
        vToken.markAsUsed();
        verificationTokenRepository.save(vToken);

        return true;
    }

    private boolean shouldActivateUser(User user) {
        // User should be activated if at least one contact method is verified
        return (user.getEmail() == null || user.getEmailVerified()) ||
                (user.getPhone() == null || user.getPhoneVerified());
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }

    private String generateSmsToken() {
        // Generate 6-digit OTP for SMS
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    public User updateUserProfile(User user, ProfileUpdateRequest request) {
        return userService.updateProfile(user, request);
    }

    public UserResponse convertToUserResponse(User user) {
        return userService.convertToUserResponse(user);
    }
}
