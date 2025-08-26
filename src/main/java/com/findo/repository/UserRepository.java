package com.findo.repository;

import com.findo.model.User;
import com.findo.model.enums.UserRole;
import com.findo.model.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

        Optional<User> findByEmail(String email);

        Optional<User> findByPhone(String phone);

        @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier")
        Optional<User> findByEmailOrPhone(@Param("identifier") String email, @Param("identifier") String phone);

        boolean existsByEmail(String email);

        boolean existsByPhone(String phone);

        Page<User> findByRole(UserRole role, Pageable pageable);

        Page<User> findByStatus(UserStatus status, Pageable pageable);

        @Query("SELECT u FROM User u WHERE " +
                        "(:role IS NULL OR u.role = :role) AND " +
                        "(:status IS NULL OR u.status = :status) AND " +
                        "(:searchTerm IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
                        "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
        Page<User> findUsersWithFilters(@Param("role") UserRole role,
                        @Param("status") UserStatus status,
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);

        @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
        long countByRole(@Param("role") UserRole role);

        @Query("SELECT COUNT(u) FROM User u WHERE u.status = :status")
        long countByStatus(@Param("status") UserStatus status);
}
