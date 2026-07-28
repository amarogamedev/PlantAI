package com.amarogamedev.plantai.service;

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

    public Plant create(Plant plant) {
        return plantRepository.save(plant);
    }

    public List<Plant> list() {
        return plantRepository.findAll();
    }

    public Optional<Plant> get(Long id) {
        return plantRepository.findById(id);
    }

    public boolean delete(Long id) {
        if (!plantRepository.existsById(id)) {
            return false;
        }
        plantRepository.deleteById(id);
        return true;
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

}
