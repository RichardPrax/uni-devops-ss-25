package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.TrainingSessionEntity;
import org.junit.jupiter.api.BeforeEach;

public abstract class TrainingSessionControllerBase extends BaseIT {

    protected TrainingSessionEntity trainingSession;
    protected TrainingPlanEntity trainingPlan;
    protected CustomExerciseEntity squatCustomEntity;
    protected GeneralExerciseEntity squatEntity;
    protected NewTrainingSessionDTO newTrainingSessionDTO;

    @BeforeEach
    public void setUpChild() {
        squatEntity = BaseData.getSquatGeneralExerciseEntity();
        generalExerciseRepository.save(squatEntity);

        trainingPlan = BaseData.getTrainingPlanEntity(john);
        squatCustomEntity = BaseData.getSquatCustomExerciseEntity(trainingPlan, squatEntity);

        trainingPlanRepository.save(trainingPlan);
        customExerciseRepository.save(squatCustomEntity);

        trainingSession = BaseData.getTrainingSessionEntity(trainingPlan, john);
        trainingSessionRepository.save(trainingSession);

        newTrainingSessionDTO = BaseData.getNewTrainingSessionDTO();
    }
}
