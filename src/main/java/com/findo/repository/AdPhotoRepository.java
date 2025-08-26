package com.findo.repository;

import com.findo.model.Ad;
import com.findo.model.AdPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdPhotoRepository extends JpaRepository<AdPhoto, String> {

    List<AdPhoto> findByAdOrderBySortOrderAsc(Ad ad);

    @Query("SELECT p FROM AdPhoto p WHERE p.ad.id = :adId ORDER BY p.sortOrder ASC")
    List<AdPhoto> findByAdIdOrderBySortOrder(@Param("adId") String adId);

    Optional<AdPhoto> findByAdAndSortOrder(Ad ad, Integer sortOrder);

    @Query("SELECT p FROM AdPhoto p WHERE p.ad = :ad AND p.sortOrder = 0")
    Optional<AdPhoto> findMainPhotoByAd(@Param("ad") Ad ad);

    long countByAd(Ad ad);

    @Query("SELECT MAX(p.sortOrder) FROM AdPhoto p WHERE p.ad = :ad")
    Integer findMaxSortOrderByAd(@Param("ad") Ad ad);
}
