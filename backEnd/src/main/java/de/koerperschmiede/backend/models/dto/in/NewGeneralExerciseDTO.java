package de.koerperschmiede.backend.models.dto.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record NewGeneralExerciseDTO(
    @NotBlank(message = "Name must not be blank")
    String name,

    @NotEmpty(message = "At least one category must be provided")
    List<String> categories,

    List<String> equipment,

    @NotBlank(message = "Short description must not be blank")
    String shortDescription,

    @NotBlank(message = "Long description must not be blank")
    String longDescription,

    @NotBlank(message = "Directions must not be blank")
    String directions,

    @NotBlank(message = "Video URL must not be blank")
    String video,

    @NotBlank(message = "Thumbnail URL must not be blank")
    String thumbnailUrl
) {
}

