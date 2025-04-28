package de.koerperschmiede.backend.models.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record NewTrainingPlanDTO(
    @NotBlank(message = "Name must not be blank")
    String name,

    @NotNull(message = "Exercises list must not be null")
    List<NewCustomExerciseDTO> exercises,

    @NotBlank(message = "Short description must not be blank")
    String shortDescription,

    @NotBlank(message = "Long description must not be blank")
    String longDescription,

    @NotNull(message = "User ID must not be null")
    UUID userId,

    String tip
) {
}


