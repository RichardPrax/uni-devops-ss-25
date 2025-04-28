package de.koerperschmiede.backend.controller.rest.TrainingPlanController;

import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DELETETrainingPlanIT extends TrainingPlanControllerBase {

    @Test
    void givenTrainingPlanId_whenUserTryToDeleteTrainingPlan_thenReturnForbidden() throws Exception {
        // check if training plan exists
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk());

        // try to delete training plan
        mockMvc.perform(delete(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden());

        // check that training plan still exist
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    void givenTrainingPlanId_whenAdminTryToDeleteTrainingPlan_thenReturnNoContent() throws Exception {
        // check if training plan exists
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk());

        // delete training plan
        mockMvc.perform(delete(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        // check that training plan does not exist anymore
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}
