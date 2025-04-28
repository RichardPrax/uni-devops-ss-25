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
    void givenDatabaseWithGeneralExercises_whenAdminTryToGetGeneralExercises_thenReturnListOfGeneralExercises() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$.[0].name").value(SQUAT_NAME))
            .andExpect(jsonPath("$.[0].categories").isArray())
            .andExpect(jsonPath("$.[0].categories[0]").value(SQUAT_CATEGORIES_ENUM.getFirst().name()))
            .andExpect(jsonPath("$.[0].equipment").isArray())
            .andExpect(jsonPath("$.[0].equipment[0]").value(SQUAT_EQUIPMENT_ENUM.getFirst().name()))
            .andExpect(jsonPath("$.[0].shortDescription").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$.[0].longDescription").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$.[0].directions").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$.[0].video").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$.[0].thumbnailUrl").value(SQUAT_THUMBNAIL_URL));

    }

    @Test
    void givenEmptyDatabase_whenUserTryToGetGeneralExercises_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
