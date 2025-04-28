package de.koerperschmiede.backend.controller.rest.CustomExerciseController;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DELETECustomExerciseIT extends CustomExerciseControllerBase {

    @Test
    void givenExistingCustomExercise_whenDeleteCustomExercise_thenReturnNoContentAndDeleteCustomExercise() throws Exception {
        // make sure custom exercise exists before deleting
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk());

        mockMvc.perform(delete(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        // make sure custom exercise is deleted
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Custom exercise with id: " + SQUAT_ID_CUSTOM + " not found"));
    }

    @Test
    void givenUnknownCustomExerciseId_whenDeleteCustomExercise_thenReturnNoContentAndDoNothing() throws Exception {
        UUID id = UUID.randomUUID();
        // custom exercise does not exists
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + id)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Custom exercise with id: " + id + " not found"));

        mockMvc.perform(delete(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + id)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());
    }
}
