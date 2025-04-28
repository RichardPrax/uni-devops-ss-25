package de.koerperschmiede.backend.controller.rest.CustomExerciseController;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class POSTCustomExerciseIT extends CustomExerciseControllerBase {

    // clear db before each test, to check if db is empty when request should fail
    @BeforeEach
    void setUpSpecific() {
        customExerciseRepository.deleteAll();
    }

    @Test
    void givenTrainingPlanWithNoCustomExercises_whenAdminTryToCreateCustomExercise_thenReturnCreatedCustomExercise() throws Exception {

        // check that training plan has no exercises
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .param("trainingPlanId", TRAINING_PLAN_ID.toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(post(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomSquatDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
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
            .andExpect(jsonPath("$['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['tip']").value(SQUAT_TIP));

        // check if training plan has an exercise
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .param("trainingPlanId", TRAINING_PLAN_ID.toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void givenTrainingPlanWithNoCustomExercises_whenAdminTryToCreateCustomExerciseWithInvalidValues_thenReturnBadRequestAndDoNotCreateCustomExercise() throws Exception {
        // create new general exercise with invalid name
        int invalidReps = 0;
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            invalidReps,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            TRAINING_PLAN_ID,
            null
        );

        // check that training plan has no exercises
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .param("trainingPlanId", TRAINING_PLAN_ID.toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

        mockMvc.perform(post(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomExerciseDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Repetitions must be at least 1"));

        // check that training plan has still no exercises
        mockMvc.perform(get(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .param("trainingPlanId", TRAINING_PLAN_ID.toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenUnknownTrainingPlanId_whenAdminTryToCreateCustomExercise_thenReturnNotFound() throws Exception {
        UUID unknownTrainingPlanId = UUID.randomUUID();

        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            SQUAT_REPS,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            unknownTrainingPlanId,
            null
        );

        mockMvc.perform(post(CUSTOM_EXERCISE_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCustomExerciseDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training plan with id: " + unknownTrainingPlanId + " not found"));
    }
}
