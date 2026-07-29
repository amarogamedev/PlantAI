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
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Schema(description = "Number of days between each watering. Used to calculate when the plant should be watered again.")
    @Column(name = "watering_interval_days")
    private Integer wateringIntervalDays;

    @Schema(description = "Date when the plant was last watered. Used together with the watering interval to determine watering needs.")
    @Column(name = "last_watered")
    private LocalDate lastWatered;

    @Schema(description = "Date when this plant record was created in the system.")
    @Column(name = "created_at")
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
    }
}