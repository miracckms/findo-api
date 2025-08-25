package com.findo.controller;

import com.findo.dto.response.AdResponse;
import com.findo.model.Ad;
import com.findo.model.enums.AdStatus;
import com.findo.repository.AdRepository;
import com.findo.service.AdService;
import com.findo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/ads/pending")
    public ResponseEntity<Page<Ad>> getPendingAds(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Ad> ads = adRepository.findPendingAds(pageable);
        return ResponseEntity.ok(ads);
    }

    @PostMapping("/ads/{id}/approve")
    public ResponseEntity<?> approveAd(@PathVariable UUID id) {
        try {
            Optional<Ad> adOptional = adRepository.findById(id);
            if (adOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ad ad = adOptional.get();
            if (ad.getStatus() != AdStatus.PENDING_APPROVAL) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid status");
                error.put("message", "Ad is not pending approval");
                return ResponseEntity.badRequest().body(error);
            }

            ad.setStatus(AdStatus.ACTIVE);
            ad.setPublishedAt(LocalDateTime.now());
            ad.setExpiresAt(LocalDateTime.now().plusDays(30)); // 30 days expiry
            adRepository.save(ad);

            // Send notification email
            if (ad.getUser().getEmail() != null && ad.getUser().getEmailVerified()) {
                emailService.sendAdApprovedEmail(ad.getUser().getEmail(), ad.getTitle());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad approved successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to approve ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/ads/{id}/reject")
    public ResponseEntity<?> rejectAd(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        try {
            String reason = request.get("reason");
            if (reason == null || reason.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Rejection reason is required");
                return ResponseEntity.badRequest().body(error);
            }

            Optional<Ad> adOptional = adRepository.findById(id);
            if (adOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Ad ad = adOptional.get();
            if (ad.getStatus() != AdStatus.PENDING_APPROVAL) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid status");
                error.put("message", "Ad is not pending approval");
                return ResponseEntity.badRequest().body(error);
            }

            ad.setStatus(AdStatus.REJECTED);
            ad.setRejectionReason(reason);
            adRepository.save(ad);

            // Send notification email
            if (ad.getUser().getEmail() != null && ad.getUser().getEmailVerified()) {
                emailService.sendAdRejectedEmail(ad.getUser().getEmail(), ad.getTitle(), reason);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Ad rejected successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to reject ad");
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalAds", adRepository.count());
        stats.put("activeAds", adRepository.countByStatus(AdStatus.ACTIVE));
        stats.put("pendingAds", adRepository.countByStatus(AdStatus.PENDING_APPROVAL));
        stats.put("rejectedAds", adRepository.countByStatus(AdStatus.REJECTED));

        return ResponseEntity.ok(stats);
    }
}
