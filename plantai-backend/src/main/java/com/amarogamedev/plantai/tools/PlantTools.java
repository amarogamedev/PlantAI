package com.amarogamedev.plantai.tools;

import com.amarogamedev.plantai.entity.Plant;
import com.amarogamedev.plantai.service.PlantService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

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
            Returns a list containing the plant name, species, watering schedule,
            and watering history.
            """)
    public List<Plant> listPlants() {
        return plantService.list();
    }

    @Tool(description = """
            Retrieves detailed information about a specific plant using its database ID.
            Use this when the user refers to a specific plant and you need its
            information before answering questions or performing actions.
            Returns the plant details or null if no plant exists with the given ID.
            """)
    public Plant getPlant(
            @ToolParam(description = """
                    The unique database identifier of the plant to retrieve.
                    This ID must correspond to an existing registered plant.
                    """)
            Long id
    ) {
        return plantService.get(id)
                .orElse(null);
    }

    @Tool(description = """
            Creates and registers a new plant in the user's collection.
            Use this when the user wants to add a new plant.
            The plant information should include its name, species if known,
            watering interval in days, and optionally the last watering date.
            """)
    public Plant createPlant(
            @ToolParam(description = """
                    The plant information to register.
                    Includes:
                    - name: common name used by the user
                    - species: scientific species name if available
                    - wateringIntervalDays: number of days between waterings
                    - lastWatered: date when the plant was last watered
                    """)
            Plant plant
    ) {
        return plantService.create(plant);
    }

    @Tool(description = """
            Deletes an existing plant from the user's collection.
            Use this only when the user explicitly requests removing a plant.
            Returns true if the plant was successfully deleted, otherwise false.
            """)
    public boolean deletePlant(
            @ToolParam(description = """
                    The unique database identifier of the plant to delete.
                    This must be an existing plant ID.
                    """)
            Long id
    ) {
        return plantService.delete(id);
    }

    @Tool(description = """
            Finds all plants that should be watered today based on their
            last watering date and configured watering interval.
            Use this when the user asks which plants need water, wants watering
            reminders, or needs a daily plant care checklist.
            Returns only plants whose watering schedule indicates they are due.
            """)
    public List<Plant> plantsDueToday() {
        return plantService.dueToday();
    }
}