package de.koerperschmiede.backend.models.dto.out;

import java.time.Instant;

public record TrainingSessionDTO(
    String id,
    TrainingPlanDTO trainingPlan,
    Instant date,
    String notes
) {
}
