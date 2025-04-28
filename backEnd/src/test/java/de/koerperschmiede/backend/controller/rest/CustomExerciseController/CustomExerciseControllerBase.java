package de.koerperschmiede.backend.controller.rest.CustomExerciseController;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import org.junit.jupiter.api.BeforeEach;

public abstract class CustomExerciseControllerBase extends BaseIT {

    protected GeneralExerciseEntity squatEntity;
    protected CustomExerciseEntity squatCustomEntity;
    protected NewCustomExerciseDTO newCustomSquatDTO;
    protected TrainingPlanEntity trainingPlan;

    /**
     * Set up the test data.
     * Create a general exercise entity and a custom exercise entity.
     * Create a training plan entity, because a custom exercise entity can only exist in a training plan.
     */
    @BeforeEach
    public void setUpChild() {
        squatEntity = BaseData.getSquatGeneralExerciseEntity();
        generalExerciseRepository.save(squatEntity);

        trainingPlan = BaseData.getTrainingPlanEntity(john);

        squatCustomEntity = BaseData.getSquatCustomExerciseEntity(trainingPlan, squatEntity);

        trainingPlanRepository.save(trainingPlan);
        customExerciseRepository.save(squatCustomEntity);

        newCustomSquatDTO = BaseData.getSquadNewCustomExerciseDTO(trainingPlan.getId());
    }
}
