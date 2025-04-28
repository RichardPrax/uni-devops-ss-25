package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import de.koerperschmiede.backend.models.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;

public abstract class BaseData {

    public static GeneralExerciseEntity getSquatGeneralExerciseEntity() {
        GeneralExerciseEntity squat = GeneralExerciseEntity.of(
            SQUAT_NAME,
            SQUAT_CATEGORIES_ENUM,
            SQUAT_EQUIPMENT_ENUM,
            SQUAT_DESCRIPTION_SHORT,
            SQUAT_DESCRIPTION_LONG,
            SQUAT_DIRECTIONS,
            SQUAT_VIDEO,
            SQUAT_THUMBNAIL_URL
        );
        squat.setId(SQUAT_ID_GENERAL);
        return squat;
    }

    public static NewGeneralExerciseDTO getSquatNewGeneralExerciseDTO() {
        return new NewGeneralExerciseDTO(
            SQUAT_NAME,
            SQUAT_CATEGORIES_STRINGS,
            SQUAT_EQUIPMENT_STRINGS,
            SQUAT_DESCRIPTION_SHORT,
            SQUAT_DESCRIPTION_LONG,
            SQUAT_DIRECTIONS,
            SQUAT_VIDEO,
            SQUAT_THUMBNAIL_URL
        );
    }

    public static CustomExerciseEntity getSquatCustomExerciseEntity(TrainingPlanEntity trainingPlan, GeneralExerciseEntity generalExercise) {
        CustomExerciseEntity customSquat = CustomExerciseEntity.of(
            generalExercise,
            trainingPlan,
            SQUAT_REPS,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP
        );
        customSquat.setId(SQUAT_ID_CUSTOM);

        return customSquat;
    }

    public static NewCustomExerciseDTO getSquadNewCustomExerciseDTO(UUID trainingPlanId) {
        return new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            SQUAT_REPS,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            trainingPlanId,
            null
        );
    }

    public static TrainingPlanEntity getTrainingPlanEntity(UserEntity user) {
        TrainingPlanEntity trainingPlan = TrainingPlanEntity.of(
            TRAINING_PLAN_NAME,
            new ArrayList<>(),
            user,
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            TRAINING_PLAN_TIP
        );

        trainingPlan.setId(TRAINING_PLAN_ID);
        return trainingPlan;
    }

    /**
     * Creates a new training plan DTO for user John.
     * Exercises contain a squat exercise.
     *
     * @return a valid NewTrainingPlanDTO object
     */
    public static NewTrainingPlanDTO getNewTrainingPlanDTO() {
        return new NewTrainingPlanDTO(
            TRAINING_PLAN_NAME,
            List.of(getSquadNewCustomExerciseDTO(null)),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );
    }

    public static TrainingSessionEntity getTrainingSessionEntity(TrainingPlanEntity trainingPlan, UserEntity user) {
        TrainingSessionEntity trainingSession = TrainingSessionEntity.of(
            trainingPlan,
            user,
            TRAINING_SESSION_DATE,
            TRAINING_SESSION_NOTES
        );
        trainingSession.setId(TRAINING_SESSION_ID);
        return trainingSession;
    }

    /**
     * Creates a new training session DTO for user John.
     * Contains the dummy training plan object id
     *
     * @return a valid NewTrainingSessionDTO object
     */
    public static NewTrainingSessionDTO getNewTrainingSessionDTO() {
        return new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            TRAINING_SESSION_DATE,
            TRAINING_SESSION_NOTES
        );
    }
}
