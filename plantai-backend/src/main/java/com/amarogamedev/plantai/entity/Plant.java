package com.amarogamedev.plantai.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "plants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the plant record in the database.")
    private Long id;

    @Schema(description = "Custom name of the plant, used for identification by the user. Example: 'Monstera', 'Bob', 'Rose'.")
    private String name;

    @Schema(description = "Scientific species name of the plant. Example: 'Monstera deliciosa'.")
    private String species;

    @Schema(description = "Number of days between each watering. Used to calculate when the plant should be watered again.")
    private Integer wateringIntervalDays;

    @Schema(description = "Date when the plant was last watered. Used together with the watering interval to determine watering needs.")
    private LocalDate lastWatered;

    @Schema(description = "Date when this plant record was created in the system.")
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
    }
}