package com.findo.repository;

import com.findo.model.User;
import com.findo.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByTokenAndTokenType(String token, String tokenType);

    List<VerificationToken> findByUserAndTokenType(User user, String tokenType);

    @Query("SELECT vt FROM VerificationToken vt WHERE vt.user = :user AND vt.tokenType = :tokenType AND vt.used = false AND vt.expiresAt > :currentTime")
    List<VerificationToken> findValidTokensByUserAndType(@Param("user") User user,
            @Param("tokenType") String tokenType,
            @Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT vt FROM VerificationToken vt WHERE vt.expiresAt < :currentTime AND vt.used = false")
    List<VerificationToken> findExpiredTokens(@Param("currentTime") LocalDateTime currentTime);

    void deleteByUser(User user);

    void deleteByUserAndTokenType(User user, String tokenType);
}
