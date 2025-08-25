package com.findo.repository;

import com.findo.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findByActiveTrue();

    List<Category> findByParentIsNullAndActiveTrue();

    List<Category> findByParentAndActiveTrue(Category parent);

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findByParentIdAndActiveTrue(@Param("parentId") UUID parentId);

    Optional<Category> findByNameAndActiveTrue(String name);

    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findAllActiveOrderBySortOrder();

    @Query("SELECT c FROM Category c LEFT JOIN c.ads a WHERE c.active = true GROUP BY c.id ORDER BY COUNT(a) DESC")
    List<Category> findCategoriesOrderByAdCount();

    boolean existsByNameAndActiveTrue(String name);
}
