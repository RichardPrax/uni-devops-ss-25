package de.koerperschmiede.backend.controller.rest.TrainingPlanController;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import org.junit.jupiter.api.BeforeEach;

public abstract class TrainingPlanControllerBase extends BaseIT {

    protected TrainingPlanEntity trainingPlan;
    protected CustomExerciseEntity squatCustomEntity;
    protected GeneralExerciseEntity squatEntity;
    protected NewTrainingPlanDTO newTrainingPlanDTO;

    @BeforeEach
    public void setUpChild() {
        squatEntity = BaseData.getSquatGeneralExerciseEntity();
        generalExerciseRepository.save(squatEntity);

        trainingPlan = BaseData.getTrainingPlanEntity(john);
        squatCustomEntity = BaseData.getSquatCustomExerciseEntity(trainingPlan, squatEntity);

        trainingPlanRepository.save(trainingPlan);
        customExerciseRepository.save(squatCustomEntity);

        newTrainingPlanDTO = BaseData.getNewTrainingPlanDTO();
    }
}
