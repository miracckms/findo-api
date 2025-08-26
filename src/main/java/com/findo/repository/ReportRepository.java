package com.findo.repository;

import com.findo.model.Ad;
import com.findo.model.Report;
import com.findo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, String> {

    Page<Report> findByStatus(String status, Pageable pageable);

    Page<Report> findByReporter(User reporter, Pageable pageable);

    Page<Report> findByAd(Ad ad, Pageable pageable);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.ad = :ad AND r.status = 'PENDING'")
    long countPendingReportsByAd(@Param("ad") Ad ad);

    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = :status")
    long countByStatus(@Param("status") String status);

    boolean existsByReporterAndAd(User reporter, Ad ad);
}
