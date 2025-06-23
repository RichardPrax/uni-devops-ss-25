package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETGeneralExercisesIT extends GeneralExerciseControllerBase {
    @Test
    void givenEmptyDatabase_whenAdminTryToGetGeneralExercises_thenReturnEmptyList() throws Exception {
        generalExerciseRepository.deleteAll();

        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    void givenEmptyDatabase_whenUserTryToGetGeneralExercises_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
