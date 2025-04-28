package de.koerperschmiede.backend.controller.rest.TrainingPlanController;

import de.koerperschmiede.backend.controller.BaseData;
import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PUTTrainingPlanIT extends TrainingPlanControllerBase {

    @Test
    void givenValidTrainingPlan_whenUserTryToUpdateAnTrainingPlan_thenReturnForbidden() throws Exception {

        mockMvc.perform(put(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(newTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenTrainingPlanWithInvalidName_whenAdminUpdatesTrainingPlan_thenReturnBadRequest() throws Exception {
        String invalidName = "";

        NewTrainingPlanDTO updatedTrainingPlanDTO = new NewTrainingPlanDTO(
            invalidName,
            List.of(BaseData.getSquadNewCustomExerciseDTO(TRAINING_PLAN_ID)),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(put(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Name must not be blank"));
    }

    @Test
    void givenInvalidTrainingPLanId_whenAdminUpdatesTrainingPlan_thenReturnNotFound() throws Exception {
        UUID invalidId = UUID.randomUUID();

        NewTrainingPlanDTO updatedTrainingPlanDTO = new NewTrainingPlanDTO(
            TRAINING_PLAN_NAME,
            List.of(BaseData.getSquadNewCustomExerciseDTO(TRAINING_PLAN_ID)),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(put(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + invalidId)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training plan with id: " + invalidId + " not found"));
    }

    @Test
    void givenTrainingPlanWithInvalidGeneralExerciseIdInNewCustomExercises_whenAdminUpdatesTrainingPlan_thenReturnNotFound() throws Exception {
        UUID invalidId = UUID.randomUUID();

        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(
            invalidId,
            SQUAT_REPS,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            TRAINING_PLAN_ID,
            null
        );

        NewTrainingPlanDTO updatedTrainingPlanDTO = new NewTrainingPlanDTO(
            TRAINING_PLAN_NAME,
            List.of(newCustomExerciseDTO),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(put(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("General exercise with id: " + invalidId + " not found"));
    }

    @Test
    void givenValidTrainingPlan_whenAdminUpdatesTrainingPlanUser_thenReturnUpdatedTrainingPlan() throws Exception {
        String newName = "Updated Plan";
        int newReps = 9;

        NewCustomExerciseDTO squatNewCustomExerciseDTO = new NewCustomExerciseDTO(
            SQUAT_ID_GENERAL,
            newReps,
            SQUAT_SETS,
            SQUAT_DURATION_IN_MINUTES,
            SQUAT_TIP,
            TRAINING_PLAN_ID,
            SQUAT_ID_CUSTOM
        );

        NewCustomExerciseDTO secondSquatNewCustomExerciseDTO = BaseData.getSquadNewCustomExerciseDTO(TRAINING_PLAN_ID);

        NewTrainingPlanDTO updatedTrainingPlanDTO = new NewTrainingPlanDTO(
            newName,
            List.of(squatNewCustomExerciseDTO, secondSquatNewCustomExerciseDTO),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(put(TRAINING_PLAN_CONTROLLER_BASE_URL + "/" + TRAINING_PLAN_ID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(updatedTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_PLAN_ID.toString()))
            .andExpect(jsonPath("$['name']").value(newName))
            .andExpect(jsonPath("$['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$['exercises']").isArray())
            .andExpect(jsonPath("$['exercises'].length()").value(2))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['exercises'][0]['repetitions']").value(newReps))
            .andExpect(jsonPath("$['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['exercises'][0]['tip']").value(SQUAT_TIP));
    }
}
