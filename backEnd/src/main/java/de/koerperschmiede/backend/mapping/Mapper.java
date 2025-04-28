package de.koerperschmiede.backend.mapping;

import de.koerperschmiede.backend.models.dto.out.*;
import de.koerperschmiede.backend.models.entities.*;
import de.koerperschmiede.backend.util.Category;
import de.koerperschmiede.backend.util.Equipment;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Mapper {
    private static final DateTimeFormatter defaultDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static UserDTO userEntityToUserDTO(UserEntity userEntity) {
        return new UserDTO(userEntity.getId().toString(),
            userEntity.getFirstName(),
            userEntity.getLastName(),
            userEntity.getBirthdate().format(defaultDateTimeFormatter),
            userEntity.getEmail());
    }

    public static GeneralExerciseDTO generalExerciseEntityToGeneralExerciseDTO(GeneralExerciseEntity generalExerciseEntity) {
        return new GeneralExerciseDTO(
            generalExerciseEntity.getId().toString(),
            generalExerciseEntity.getName(),
            generalExerciseEntity.getCategories().stream().map(Category::toString).collect(Collectors.toList()),
            generalExerciseEntity.getEquipment().stream().map(Equipment::toString).collect(Collectors.toList()),
            generalExerciseEntity.getShortDescription(),
            generalExerciseEntity.getLongDescription(),
            generalExerciseEntity.getDirections(),
            generalExerciseEntity.getVideo(),
            generalExerciseEntity.getThumbNailUrl()
        );
    }

    public static TrainingPlanDTO trainingPlanEntityToTrainingPlanDTO(TrainingPlanEntity trainingPlanEntity) {
        return new TrainingPlanDTO(
            trainingPlanEntity.getId().toString(),
            trainingPlanEntity.getName(),
            trainingPlanEntity.getExercises().stream().map(Mapper::customExerciseEntityToCustomExerciseDTO).collect(Collectors.toList()),
            trainingPlanEntity.getLongDescription(),
            trainingPlanEntity.getShortDescription(),
            trainingPlanEntity.getTip()
        );
    }

    public static TrainingSessionDTO sessionEntityToSessionDTO(TrainingSessionEntity trainingSessionEntity) {
        return new TrainingSessionDTO(
            trainingSessionEntity.getId().toString(),
            trainingPlanEntityToTrainingPlanDTO(trainingSessionEntity.getTrainingPlan()),
            trainingSessionEntity.getDate(),
            trainingSessionEntity.getNotes()
        );
    }

    public static CustomExerciseDTO customExerciseEntityToCustomExerciseDTO(CustomExerciseEntity customExerciseEntity) {
        return new CustomExerciseDTO(
            customExerciseEntity.getId().toString(),
            generalExerciseEntityToGeneralExerciseDTO(customExerciseEntity.getGeneralExercise()),
            customExerciseEntity.getRepetitions(),
            customExerciseEntity.getSets(),
            customExerciseEntity.getDurationInMinutes(),
            customExerciseEntity.getTip()
        );
    }
}
