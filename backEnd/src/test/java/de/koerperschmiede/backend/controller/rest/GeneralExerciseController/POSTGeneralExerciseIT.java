package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class POSTGeneralExerciseIT extends GeneralExerciseControllerBase {

    // clear db before each test, to check if db is empty when request should fail
    @BeforeEach
    void setUpSpecific() {
        generalExerciseRepository.deleteAll();
    }

    @Test
    void givenEmptyDatabase_whenAdminTryToCreateGeneralExercise_thenReturnCreatedGeneralExercise() throws Exception {
        mockMvc.perform(post(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newSquatDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(SQUAT_NAME));

        // check if database has entry
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value(SQUAT_NAME));
    }

    @Test
    void givenEmptyDatabase_whenAdminTryToCreateGeneralExerciseWithInvalidValues_thenReturnBadRequest() throws Exception {
        // create new general exercise with invalid name
        String invalidName = "";
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(
            invalidName,
            SQUAT_CATEGORIES_STRINGS,
            SQUAT_EQUIPMENT_STRINGS,
            SQUAT_DESCRIPTION_SHORT,
            SQUAT_DESCRIPTION_LONG,
            SQUAT_DIRECTIONS,
            SQUAT_VIDEO,
            SQUAT_THUMBNAIL_URL
        );

        mockMvc.perform(post(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newGeneralExerciseDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Name must not be blank"));

        // check if database is still empty
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenEmptyDatabase_whenUserTryToCreateGeneralExercise_thenReturnForbidden() throws Exception {
        mockMvc.perform(post(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newSquatDTO)))
            .andDo(print())
            .andExpect(status().isForbidden());

        // check if database is still empty
        mockMvc.perform(get(GENERAL_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }
}
