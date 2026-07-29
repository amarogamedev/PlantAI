package com.amarogamedev.plantai.dto;

import org.springframework.context.annotation.Description;

import java.time.LocalDate;

public record CreatePlantDTO(

        @Description("Display name chosen by the user, e.g. 'Rose in the backyard'.")
        String name,

        @Description("Watering interval in days.")
        Integer wateringIntervalDays,

        @Description("Date the plant was last watered.")
        LocalDate lastWatered
) { }