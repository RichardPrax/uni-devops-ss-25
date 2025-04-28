package de.koerperschmiede.backend.controller.rest.GeneralExerciseController;

import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PUTGeneralExerciseIT extends GeneralExerciseControllerBase {

    @Test
    void givenDatabaseWithGeneralExercise_whenAdminTryToUpdateGeneralExercise_thenReturnUpdatedGeneralExercise() throws Exception {
        String newName = "BackSquat";
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(
            newName,
            SQUAT_CATEGORIES_STRINGS,
            SQUAT_EQUIPMENT_STRINGS,
            SQUAT_DESCRIPTION_SHORT,
            SQUAT_DESCRIPTION_LONG,
            SQUAT_DIRECTIONS,
            SQUAT_VIDEO,
            SQUAT_THUMBNAIL_URL
        );

        mockMvc.perform(put(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newGeneralExerciseDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    void givenEmptyDatabase_whenAdminTryToUpdateGeneralExercise_thenReturnNotFound() throws Exception {
        generalExerciseRepository.deleteAll();

        mockMvc.perform(put(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newSquatDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("General exercise with id " + SQUAT_ID_GENERAL + " not found"));
    }

    @Test
    void givenDatabaseWithGeneralExercise_whenAdminTryToUpdateGeneralExerciseWithInvalidExercise_thenReturnBadRequest() throws Exception {
        String newName = "";
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(
            newName,
            SQUAT_CATEGORIES_STRINGS,
            SQUAT_EQUIPMENT_STRINGS,
            SQUAT_DESCRIPTION_SHORT,
            SQUAT_DESCRIPTION_LONG,
            SQUAT_DIRECTIONS,
            SQUAT_VIDEO,
            SQUAT_THUMBNAIL_URL
        );

        mockMvc.perform(put(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newGeneralExerciseDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Name must not be blank"));
    }

    @Test
    void givenUser_whenTryToAccessEndpoint_thenReturnForbidden() throws Exception {

        mockMvc.perform(put(GENERAL_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_GENERAL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newSquatDTO)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
