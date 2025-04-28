package de.koerperschmiede.backend.models.dto.out;

import java.util.List;

public record GeneralExerciseDTO(
    String id,
    String name,
    List<String> categories,
    List<String> equipment,
    String shortDescription,
    String longDescription,
    String directions,
    String video,
    String thumbnailUrl
) {
}

