package de.koerperschmiede.backend.controller.rest.CustomExerciseController;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETCustomExerciseIT extends CustomExerciseControllerBase {

    @Test
    void givenEmptyDatabase_whenAdminTryToGetCustomExercises_thenReturnNotFound() throws Exception {
        customExerciseRepository.deleteAll();

        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Custom exercise with id: " + SQUAT_ID_CUSTOM + " not found"));
    }

    @Test
    void givenDatabaseWithCustomExercises_whenAdminTryToGetCustomExercisesWithIdThatDoesNotExists_thenReturnNotFound() throws Exception {
        String randomCustomExerciseId = UUID.randomUUID().toString();

        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + randomCustomExerciseId)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Custom exercise with id: " + randomCustomExerciseId + " not found"));
    }

    @Test
    void givenDatabaseWithCustomExercise_whenAdminTryToGetCustomExercise_thenReturnCustomExercise() throws Exception {
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenDatabaseWithCustomExercise_whenUserTryToGetExerciseForHisTrainingPlan_thenReturnCustomExercise() throws Exception {
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenDatabaseWithCustomExercise_whenUserTryToGetExerciseForOtherUserTrainingPlan_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userJaneJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }
}
