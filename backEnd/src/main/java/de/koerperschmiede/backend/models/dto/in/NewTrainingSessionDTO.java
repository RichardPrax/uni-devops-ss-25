package de.koerperschmiede.backend.models.dto.in;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record NewTrainingSessionDTO(
    @NotNull(message = "Training plan ID must not be null")
    UUID trainingPlanId,

    @NotNull(message = "User ID must not be null")
    UUID userId,

    @NotNull(message = "Date must not be null")
    Instant date,

    String notes
) {
}

