package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import org.junit.jupiter.api.BeforeEach;

public abstract class GeneralExerciseControllerBase extends BaseIT {

    protected GeneralExerciseEntity squatEntity;
    protected NewGeneralExerciseDTO newSquatDTO;

    @BeforeEach
    public void setUpChild() {
        squatEntity = BaseData.getSquatGeneralExerciseEntity();
        generalExerciseRepository.save(squatEntity);

        newSquatDTO = BaseData.getSquatNewGeneralExerciseDTO();
    }
}
