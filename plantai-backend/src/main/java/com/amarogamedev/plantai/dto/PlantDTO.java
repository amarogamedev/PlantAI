package com.amarogamedev.plantai.dto;

import com.amarogamedev.plantai.entity.Plant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantDTO {

    private Long id;
    private String name;
    private Integer wateringIntervalDays;
    private LocalDate lastWatered;
    private LocalDate createdAt;

    public static PlantDTO fromEntity(Plant p) {
        if (p == null) return null;
        return PlantDTO.builder()
                .id(p.getId())
                .name(p.getName())
                .wateringIntervalDays(p.getWateringIntervalDays())
                .lastWatered(p.getLastWatered())
                .createdAt(p.getCreatedAt())
                .build();
    }

    public static Plant toEntity(PlantDTO dto) {
        if (dto == null) return null;
        return Plant.builder()
                .id(dto.getId())
                .name(dto.getName())
                .wateringIntervalDays(dto.getWateringIntervalDays())
                .lastWatered(dto.getLastWatered())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
