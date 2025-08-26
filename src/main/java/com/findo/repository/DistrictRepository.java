package com.findo.repository;

import com.findo.model.City;
import com.findo.model.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

    List<District> findByActiveTrue();

    List<District> findByCityAndActiveTrue(City city);

    @Query("SELECT d FROM District d WHERE d.city.id = :cityId AND d.active = true ORDER BY d.name ASC")
    List<District> findByCityIdAndActiveTrue(@Param("cityId") String cityId);

    Optional<District> findByNameAndCityAndActiveTrue(String name, City city);

    @Query("SELECT d FROM District d LEFT JOIN d.ads a WHERE d.city.id = :cityId AND d.active = true GROUP BY d.id ORDER BY COUNT(a) DESC")
    List<District> findDistrictsOrderByAdCount(@Param("cityId") String cityId);

    boolean existsByNameAndCityAndActiveTrue(String name, City city);
}
