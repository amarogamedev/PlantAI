package com.amarogamedev.plantai.service;

import com.amarogamedev.plantai.dto.CreatePlantDTO;
import com.amarogamedev.plantai.entity.Plant;
import com.amarogamedev.plantai.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Plant create(CreatePlantDTO plant) {
        Plant plantEntity = Plant.builder()
                .id(null)
                .name(plant.name())
                .wateringIntervalDays(plant.wateringIntervalDays())
                .lastWatered(plant.lastWatered() == null ? LocalDate.now() : plant.lastWatered())
                .build();
        return plantRepository.save(plantEntity);
    }

    public Plant update(Long id, CreatePlantDTO plant) {
        Plant plantEntity = findById(id).orElseThrow();

        if(plant.name() != null) {
            plantEntity.setName(plant.name());
        }
        if(plant.wateringIntervalDays() != null) {
            plantEntity.setWateringIntervalDays(plant.wateringIntervalDays());
        }
        if(plant.lastWatered() != null) {
            plantEntity.setLastWatered(plant.lastWatered());
        }

        return plantRepository.save(plantEntity);
    }

    public List<Plant> list() {
        return plantRepository.findAll();
    }

    public List<Plant> findBySimilarName(String name) {
        return plantRepository.findBySimilarName(name);
    }

    public Optional<Plant> findById(Long id) {
        return plantRepository.findById(id);
    }

    public List<Plant> dueToday() {
        LocalDate today = LocalDate.now();
        return plantRepository.findAll().stream().filter(p -> {
            if (p.getLastWatered() == null) {
                return true;
            }
            int interval = p.getWateringIntervalDays() != null ? p.getWateringIntervalDays() : 7;
            return !p.getLastWatered().plusDays(interval).isAfter(today);
        }).collect(Collectors.toList());
    }

    public boolean delete(String name) {
        if (!plantRepository.existsByName(name)) {
            return false;
        }
        plantRepository.deleteByName(name);
        return true;
    }

    public List<Plant> plantsDueBetween(LocalDate start, LocalDate end) {
        return plantRepository.findPlantsDueBetween(start, end);
    }
}
