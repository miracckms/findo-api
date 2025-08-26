package com.findo.repository;

import com.findo.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, String> {

    List<City> findByActiveTrue();

    Optional<City> findByNameAndActiveTrue(String name);

    Optional<City> findByPlateCodeAndActiveTrue(String plateCode);

    @Query("SELECT c FROM City c LEFT JOIN c.ads a WHERE c.active = true GROUP BY c.id ORDER BY COUNT(a) DESC")
    List<City> findCitiesOrderByAdCount();

    @Query("SELECT c FROM City c WHERE c.active = true ORDER BY c.name ASC")
    List<City> findAllActiveOrderByName();

    boolean existsByNameAndActiveTrue(String name);

    boolean existsByPlateCodeAndActiveTrue(String plateCode);
}
