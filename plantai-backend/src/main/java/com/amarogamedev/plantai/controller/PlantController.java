package com.amarogamedev.plantai.controller;

import com.amarogamedev.plantai.dto.CreatePlantDTO;
import com.amarogamedev.plantai.dto.PlantDTO;
import com.amarogamedev.plantai.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping
    public ResponseEntity<PlantDTO> create(@RequestBody CreatePlantDTO plant) {
        return ResponseEntity.ok(PlantDTO.fromEntity(plantService.create(plant)));
    }

    @GetMapping
    public List<PlantDTO> list() {
        return plantService.list().stream().map(PlantDTO::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> get(@PathVariable Long id) {
        return plantService.findById(id).map(PlantDTO::fromEntity).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!plantService.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/due-today")
    public List<PlantDTO> dueToday() {
        return plantService.dueToday().stream().map(PlantDTO::fromEntity).collect(Collectors.toList());
    }
}
