package com.amarogamedev.plantai.controller;

import com.amarogamedev.plantai.entity.Plant;
import com.amarogamedev.plantai.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PostMapping
    public ResponseEntity<Plant> create(@RequestBody Plant plant) {
        return ResponseEntity.ok(plantService.create(plant));
    }

    @GetMapping
    public List<Plant> list() {
        return plantService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> get(@PathVariable Long id) {
        return plantService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!plantService.delete(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/due-today")
    public List<Plant> dueToday() {
        return plantService.dueToday();
    }
}
