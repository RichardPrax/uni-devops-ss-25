package de.koerperschmiede.backend.controller.rest.TrainingPlanController;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETTrainingPlanIT extends TrainingPlanControllerBase {
    @Test
    void givenEmptyDatabase_whenAdminTryToGetUnknownTrainingPlan_thenReturnNotFound() throws Exception {
        trainingPlanRepository.deleteAll();

        UUID randomTrainingPlanId = UUID.randomUUID();

        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + randomTrainingPlanId)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training plan with id: " + randomTrainingPlanId + " not found"));
    }

    @Test
    void givenDatabaseWithTrainingPlan_whenAdminTryToGetTrainingPlanForOtherUser_thenReturnTrainingPlan() throws Exception {
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_PLAN_ID.toString()))
            .andExpect(jsonPath("$['name']").value(TRAINING_PLAN_NAME))
            .andExpect(jsonPath("$['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$['exercises']").isArray())
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['exercises'][0]['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenDatabaseWithTrainingPlan_whenUserTryToGetTrainingPlanForHimSelf_thenReturnTrainingPlan() throws Exception {
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_PLAN_ID.toString()))
            .andExpect(jsonPath("$['name']").value(TRAINING_PLAN_NAME))
            .andExpect(jsonPath("$['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$['exercises']").isArray())
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['exercises'][0]['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenDatabaseWithTrainingPlan_whenUserTryToGetTrainingPlanForOtherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJaneJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }
}

