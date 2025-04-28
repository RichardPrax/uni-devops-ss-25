package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import de.koerperschmiede.backend.controller.Constants;
import de.koerperschmiede.backend.util.Category;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class GETGeneralExerciseIT extends GeneralExerciseControllerBase {
    @Test
    void givenNotRegisteredUUID_whenAdminTryToGetGeneralExercises_thenReturnNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();

        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + randomUUID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("General exercise with id: " + randomUUID + " not found"));
    }

    @Test
    void givenDatabaseWithGeneralExercise_whenAdminTryToGetGeneralExercises_thenReturnGeneralExercise() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + Constants.SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(Constants.SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$.name").value(SQUAT_NAME));
    }

    @Test
    void givenUser_whenUserTryToAccessEndpoint_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + Constants.SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenAdmin_whenAdminTryToAccessCategories_thenReturnListOfCategories() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/categories")
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(Category.values().length));
    }

    @Test
    void givenUser_whenUserTryToAccessCategories_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/categories")
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
