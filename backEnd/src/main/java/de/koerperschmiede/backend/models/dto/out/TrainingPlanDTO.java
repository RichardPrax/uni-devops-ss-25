package de.koerperschmiede.backend.models.dto.out;

import java.util.List;

public record TrainingPlanDTO(
    String id,
    String name,
    List<CustomExerciseDTO> exercises,
    String longDescription,
    String shortDescription,
    String tip
) {
}

