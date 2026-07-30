package com.amarogamedev.plantai.components;

import com.amarogamedev.plantai.dto.CreatePlantDTO;
import com.amarogamedev.plantai.dto.PlantDTO;
import com.amarogamedev.plantai.dto.ToolResponse;
import com.amarogamedev.plantai.entity.Plant;
import com.amarogamedev.plantai.service.PlantService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlantTools {

    private final PlantService plantService;

    public PlantTools(PlantService plantService) {
        this.plantService = plantService;
    }

    @Tool(description = """
            Retrieves all plants registered in the user's plant collection.
            Use this when the user wants to see their plants, get an overview
            of their collection, or needs information about multiple plants.
            Returns a list containing the plant name, watering schedule,
            and watering history.
            """)
    public ToolResponse<List<PlantDTO>> listPlants() {
        try {
            List<PlantDTO> plants = plantService.list().stream().map(PlantDTO::fromEntity).collect(Collectors.toList());
            return ToolResponse.success(plants);
        } catch (Exception e) {
            return ToolResponse.error("LIST_PLANTS_ERROR", "Failed to retrieve the plant collection: " + e.getMessage());
        }
    }

    @Tool(description = """
        Searches for plants by name.
        Use this whenever the user refers to a plant by name before answering
        questions or performing actions on that plant.
        The search is not limited to exact matches and may return multiple plants
        with similar names.
        If exactly one plant matches, use that plant.
        If multiple plants match, DO NOT guess which one the user means, You MUST ask the user to clarify which plant they are referring to.
        If no plants match, inform the user that no matching plant was found.
        """)
    public ToolResponse<List<PlantDTO>> findPlantBySimilarName(String name) {
        try {
            List<PlantDTO> plants = plantService.findBySimilarName(name)
                    .stream()
                    .map(PlantDTO::fromEntity)
                    .toList();

            if (plants.isEmpty()) {
                return ToolResponse.error(
                        "PLANT_NOT_FOUND",
                        "No plant was found matching the name " + name
                );
            }

            return ToolResponse.success(plants);
        } catch (Exception e) {
            return ToolResponse.error(
                    "GET_PLANT_ERROR",
                    "Failed to retrieve the plant: " + e.getMessage()
            );
        }
    }

    @Tool(description = """
            Creates and registers a new plant in the user's collection.
            Use this when the user wants to add a new plant.
            The plant information should include its name,
            watering interval in days, and optionally the last watering date.
            The plant's name must be unique, this method may throw an error if another plant with the same name exists.
            If the user uses a relative date (today, yesterday, tomorrow, etc.)
            you MUST refer to the system prompt to know what day it is before filling lastWatered.
            Do NOT assume any information without asking the user first, always ask for all the parameters necessary to create a plant.
            """)
    public ToolResponse<PlantDTO> createPlant(CreatePlantDTO plant) {
        try {
            Plant created = plantService.create(plant);
            return ToolResponse.success(PlantDTO.fromEntity(created));
        } catch (Exception e) {
            return ToolResponse.error("CREATE_PLANT_ERROR", "Failed to create the plant: " + e.getMessage());
        }
    }

    @Tool(description = """
            Deletes an existing plant from the user's collection by name.
            Use this only when the user explicitly requests removing a plant.
            Returns true if the plant was successfully deleted, otherwise false.
            """)
    public ToolResponse<Boolean> deletePlant(String name) {
        try {
            boolean deleted = plantService.delete(name);
            if (!deleted) {
                return ToolResponse.error("PLANT_NOT_FOUND", "No plant was found with name " + name);
            }
            return ToolResponse.success(true);
        } catch (Exception e) {
            return ToolResponse.error("DELETE_PLANT_ERROR", "Failed to delete the plant: " + e.getMessage());
        }
    }

    @Tool(description = """
            Finds all plants that should be watered today based on their
            last watering date and configured watering interval.
            Use this when the user asks which plants need water, wants watering
            reminders, or needs a daily plant care checklist.
            Returns only plants whose watering schedule indicates they are due.
            """)
    public ToolResponse<List<PlantDTO>> plantsDueToday() {
        try {
            List<PlantDTO> plants = plantService.dueToday().stream().map(PlantDTO::fromEntity).collect(Collectors.toList());
            return ToolResponse.success(plants);
        } catch (Exception e) {
            return ToolResponse.error("PLANTS_DUE_TODAY_ERROR", "Failed to retrieve today's watering list: " + e.getMessage());
        }
    }

    @Tool(description = """
        Returns all plants whose next watering date falls between the provided
        start and end dates (inclusive).
        Use this whenever the user asks about plants that need watering during a specific period.
        Always use this tool instead of calculating watering schedules yourself.
        The dates must be absolute ISO-8601 dates (yyyy-MM-dd).
        If the user uses a relative date (today, yesterday, tomorrow, etc.)
        you MUST refer to the system prompt to determine the correct calendar dates before calling this tool.
        """)
    public ToolResponse<List<PlantDTO>> plantsDueBetween(LocalDate start, LocalDate end) {
        try {
            List<PlantDTO> plants = plantService.plantsDueBetween(start, end)
                    .stream()
                    .map(PlantDTO::fromEntity)
                    .toList();

            return ToolResponse.success(plants);
        } catch (Exception e) {
            return ToolResponse.error(
                    "PLANTS_DUE_BETWEEN_ERROR",
                    "Failed to retrieve plants due between " + start + " and " + end + ": " + e.getMessage()
            );
        }
    }

    @Tool(description = """
            Updates an existing plant by its ID.
            Use this tool whenever the user wants to modify any information about an existing plant.
            The first parameter is the plant's ID.
            The second parameter is a partial update object. Any field may be omitted.
            Only the provided fields will be modified.
            Examples:
            
            User: "I watered my rose today."
            
            Step 1:
            Call getCurrentDate()
            
            Result:
            2026-07-29
            
            Step 2:
            Call updatePlant(
                id=1L,
                plant={
                    lastWatered="2026-07-29"
                }
            )
            
            User: "Change my rose watering interval to 5 days."
            Call:
            id = 1L
            plant = {
                wateringIntervalDays = 5
            }
            
            User: "Rename my rose to White Rose."
            Call:
            id = 1L
            plant = {
                name = "White Rose"
            }
            
            Do not ask for fields that are unrelated to the requested change.
            Only ask for clarification if the current plant name cannot be determined.
            If you change the plant's name, the new name must be unique,
            this method may throw an error if another plant with the same name exists.
            If you don't know the ID of the plant being updated, call findPlantBySimilarName using the name the user provided.
            If the user uses a relative date (today, yesterday, tomorrow, etc.)
            you MUST refer to the system prompt to know what day it is before filling lastWatered.
            """)
    public ToolResponse<PlantDTO> updatePlant(Long id, CreatePlantDTO plant) {
        try {
            Plant updated = plantService.update(id, plant);
            return ToolResponse.success(PlantDTO.fromEntity(updated));
        } catch (Exception e) {
            return ToolResponse.error("UPDATE_PLANT_ERROR", "Failed to update the plant: " + e.getMessage());
        }
    }
}