package de.koerperschmiede.backend.models.dto.out;

public record CustomExerciseDTO(
    String id,
    GeneralExerciseDTO generalExercise,
    int repetitions,
    int sets,
    int durationInMinutes,
    String tip
) {
}
