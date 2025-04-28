package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DELETEGeneralExerciseIT extends GeneralExerciseControllerBase {
    @Test
    void givenDatabaseWithGeneralExercise_whenAdminTryToDeleteGeneralExercise_thenDeleteExerciseAndReturnNoContent() throws Exception {
        // make sure general exercise exists
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk());

        // delete exercise
        mockMvc.perform(delete(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        // check if exercise got deleted
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("General exercise with id: " + SQUAT_ID_GENERAL + " not found"));
    }

    @Test
    void givenEmptyDatabase_whenAdminTryToDeleteGeneralExercise_thenReturnNoContent() throws Exception {
        mockMvc.perform(delete(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void givenUser_whenUserTryToDeleteGeneralExercise_thenForbidden() throws Exception {
        mockMvc.perform(delete(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

}
