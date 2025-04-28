package de.koerperschmiede.backend.controller.usecases;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GeneralExerciseUseCaseIT extends BaseIT {

    @Test
    void givenGeneralExerciseAndTrainingPLanWithCustomExercise_whenAdminDeletingGeneralExercise_thenAlsoDeleteCustomExercise() throws Exception {
        // given
        GeneralExerciseEntity squat = BaseData.getSquatGeneralExerciseEntity();
        generalExerciseRepository.save(squat);

        // when persisting a training plan, the custom exercise will be persisted as well
        TrainingPlanEntity trainingPlan = BaseData.getTrainingPlanEntity(john);

        CustomExerciseEntity squatCustomExercise = BaseData.getSquatCustomExerciseEntity(trainingPlan, squat);
        trainingPlan.getExercises().add(squatCustomExercise);

        trainingPlanRepository.save(trainingPlan);

        // when
        mockMvc.perform(delete(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertTrue(customExerciseRepository.findAll().isEmpty());
        assertTrue(generalExerciseRepository.findAll().isEmpty());
    }
}
