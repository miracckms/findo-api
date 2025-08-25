package com.findo.repository;

import com.findo.model.Ad;
import com.findo.model.Favorite;
import com.findo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

    Optional<Favorite> findByUserAndAd(User user, Ad ad);

    Page<Favorite> findByUser(User user, Pageable pageable);

    long countByUser(User user);

    long countByAd(Ad ad);

    boolean existsByUserAndAd(User user, Ad ad);

    void deleteByUserAndAd(User user, Ad ad);
}
