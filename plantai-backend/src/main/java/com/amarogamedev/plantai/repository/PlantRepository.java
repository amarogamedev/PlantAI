package com.amarogamedev.plantai.repository;

import com.amarogamedev.plantai.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PlantRepository extends JpaRepository<Plant, Long> {
    Optional<Plant> findByName(String name);
    boolean existsByName(String name);
    void deleteByName(String name);
    @Query("""
        SELECT p
        FROM Plant p
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        ORDER BY p.name
        """)
    List<Plant> findBySimilarName(@Param("name") String name);
    @Query(value = """
    SELECT *
    FROM plants p
    WHERE DATE_ADD(p.last_watered, INTERVAL p.watering_interval_days DAY) BETWEEN :startDate AND :endDate
    ORDER BY DATE_ADD(p.last_watered, INTERVAL p.watering_interval_days DAY)
    """, nativeQuery = true)
    List<Plant> findPlantsDueBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
