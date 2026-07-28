package com.amarogamedev.plantai.repository;

import com.amarogamedev.plantai.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
