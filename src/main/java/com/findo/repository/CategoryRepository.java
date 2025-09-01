package com.findo.repository;

import com.findo.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<Category> findByActiveTrue();

    List<Category> findByParentIsNullAndActiveTrue();

    List<Category> findByParentIsNull();

    Page<Category> findByParentIsNull(Pageable pageable);

    List<Category> findByParentAndActiveTrue(Category parent);

    Category getById(String id);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findByParentIdAndActiveTrue(@Param("parentId") String parentId);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId ORDER BY c.sortOrder ASC")
    List<Category> findByParentId(@Param("parentId") String parentId);

    Optional<Category> findByNameAndActiveTrue(String name);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findAllActiveOrderBySortOrder();

    @Query("SELECT c FROM Category c LEFT JOIN c.ads a WHERE c.active = true GROUP BY c.id ORDER BY COUNT(a) DESC")
    List<Category> findCategoriesOrderByAdCount();

    boolean existsByNameAndActiveTrue(String name);

    // Paginated queries for admin
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId ORDER BY c.sortOrder ASC")
    Page<Category> findByParentId(@Param("parentId") String parentId, Pageable pageable);
}
