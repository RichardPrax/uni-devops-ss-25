package de.koerperschmiede.backend.models.dto.in;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NewCustomExerciseDTO(
    @NotNull(message = "General exercise ID must not be null")
    UUID generalExerciseId,

    @Min(value = 1, message = "Repetitions must be at least 1")
    int repetitions,

    @Min(value = 1, message = "Sets must be at least 1")
    int sets,

    @Min(value = 1, message = "Duration must be at least 1 minute")
    int durationInMinutes,

    String tip,

    @Nullable
    UUID trainingPlanId,

    @Nullable
    UUID id
) {
}
