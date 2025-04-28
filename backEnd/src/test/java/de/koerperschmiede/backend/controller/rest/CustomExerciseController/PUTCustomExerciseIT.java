package de.koerperschmiede.backend.controller.rest.CustomExerciseController;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PUTCustomExerciseIT extends CustomExerciseControllerBase {

    @Test
    void givenDatabaseWithCustomExercise_whenAdminTryToUpdateCustomExercise_thenReturnUpdatedCustomExercise() throws Exception {
        int newReps = 15;
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            newReps,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            null,
            null
        );

        mockMvc.perform(put(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomExerciseDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['repetitions']").value(newReps))
            .andExpect(jsonPath("$.id").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenEmptyDatabase_whenAdminTryToUpdateCustomExercise_thenReturnNotFound() throws Exception {
        customExerciseRepository.deleteAll();

        mockMvc.perform(put(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomSquatDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Custom exercise with id: " + SQUAT_ID_CUSTOM + " not found"));
    }

    @Test
    void givenDatabaseWithCustomExercise_whenAdminTryToUpdateCustomExerciseWithInvalidData_thenReturnBadRequest() throws Exception {
        int invalidReps = -5;
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            invalidReps,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            null,
            null
        );

        mockMvc.perform(put(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomExerciseDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Repetitions must be at least 1"));
    }

    @Test
    void givenDatabaseWithCustomExercise_whenAdminTryToUpdateCustomExerciseWithInvalidGeneralExercise_thenReturnNotFound() throws Exception {
        UUID invalidGeneralExerciseId = UUID.randomUUID();
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            invalidGeneralExerciseId,
            SQUAT_REPS,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            null,
            null
        );

        mockMvc.perform(put(CUSTOM_EXERCISE_CONTROLLER_BASE_URL + "/" + SQUAT_ID_CUSTOM)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomExerciseDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("General exercise with id: " + invalidGeneralExerciseId + " not found"));
    }
}
