package com.findo.repository;

import com.findo.model.Ad;
import com.findo.model.User;
import com.findo.model.enums.AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, String> {

        Page<Ad> findByStatus(AdStatus status, Pageable pageable);

        Page<Ad> findByUser(User user, Pageable pageable);

        Page<Ad> findByUserAndStatus(User user, AdStatus status, Pageable pageable);

        @Query("SELECT a FROM Ad a WHERE a.status = 'ACTIVE' AND " +
                        "(:categoryId IS NULL OR a.category.id = :categoryId) AND " +
                        "(:cityId IS NULL OR a.city.id = :cityId) AND " +
                        "(:districtId IS NULL OR a.district.id = :districtId) AND " +
                        "(:minPrice IS NULL OR a.price >= :minPrice) AND " +
                        "(:maxPrice IS NULL OR a.price <= :maxPrice) AND " +
                        "(:searchTerm IS NULL OR " +
                        "LOWER(a.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(a.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<Ad> searchAds(@Param("categoryId") String categoryId,
                        @Param("cityId") String cityId,
                        @Param("districtId") String districtId,
                        @Param("minPrice") BigDecimal minPrice,
                        @Param("maxPrice") BigDecimal maxPrice,
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);

        @Query("SELECT a FROM Ad a WHERE a.status = 'ACTIVE' AND " +
                        "a.category.id IN :categoryIds")
        Page<Ad> findByCategoryIds(@Param("categoryIds") List<String> categoryIds, Pageable pageable);

        @Query("SELECT COUNT(a) FROM Ad a WHERE a.user = :user AND a.status != 'DELETED'")
        long countActiveAdsByUser(@Param("user") User user);

        @Query("SELECT a FROM Ad a WHERE a.status = 'ACTIVE' AND a.featured = true")
        Page<Ad> findFeaturedAds(Pageable pageable);

        @Query("SELECT a FROM Ad a WHERE a.status = 'ACTIVE' AND a.urgent = true")
        Page<Ad> findUrgentAds(Pageable pageable);

        @Query("SELECT a FROM Ad a WHERE a.expiresAt < :currentTime AND a.status = 'ACTIVE'")
        List<Ad> findExpiredAds(@Param("currentTime") LocalDateTime currentTime);

        @Query("SELECT COUNT(a) FROM Ad a WHERE a.status = :status")
        long countByStatus(@Param("status") AdStatus status);

        @Query("SELECT a FROM Ad a JOIN a.favorites f WHERE f.user = :user")
        Page<Ad> findFavoriteAdsByUser(@Param("user") User user, Pageable pageable);

        @Query("SELECT a FROM Ad a WHERE a.status = 'PENDING_APPROVAL' ORDER BY a.createdAt ASC")
        Page<Ad> findPendingAds(Pageable pageable);
}