package de.koerperschmiede.backend.controller.rest.TrainingPlanController;

import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.BaseData.getSquadNewCustomExerciseDTO;
import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class POSTTrainingPlanIT extends TrainingPlanControllerBase {

    @BeforeEach
    void setUpSpecific() {
        trainingPlanRepository.deleteAll();
    }

    @Test
    void givenValidTrainingPlan_whenUserTryToCreateTrainingPlan_thenReturnForbidden() throws Exception {
        mockMvc.perform(post(TRAINING_PLAN_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenValidTrainingPlan_whenAdminTryToCreateTrainingPlanForUser_thenReturnCreatedTrainingPlan() throws Exception {
        mockMvc.perform(post(TRAINING_PLAN_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(TRAINING_PLAN_NAME))
            .andExpect(jsonPath("$.shortDescription").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$.longDescription").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$.tip").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$.exercises").isArray())
            .andExpect(jsonPath("$.exercises[0].generalExercise.id").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$.exercises[0].generalExercise.name").value(SQUAT_NAME))
            .andExpect(jsonPath("$.exercises[0].generalExercise.categories").value("LOWER_BODY"))
            .andExpect(jsonPath("$.exercises[0].generalExercise.equipment").value("CHAIR"))
            .andExpect(jsonPath("$.exercises[0].generalExercise.shortDescription").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$.exercises[0].generalExercise.longDescription").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$.exercises[0].generalExercise.directions").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$.exercises[0].generalExercise.video").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$.exercises[0].generalExercise.thumbnailUrl").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$.exercises[0].repetitions").value(SQUAT_REPS))
            .andExpect(jsonPath("$.exercises[0].sets").value(SQUAT_SETS))
            .andExpect(jsonPath("$.exercises[0].durationInMinutes").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$.exercises[0].tip").value(SQUAT_TIP));
    }

    @Test
    void givenInvalidTrainingPlan_whenAdminTryToCreateTrainingPlanForUser_thenReturnBadRequest() throws Exception {
        String invalidName = "";
        NewTrainingPlanDTO invalidTrainingPlanDTO = new NewTrainingPlanDTO(
            invalidName,
            List.of(getSquadNewCustomExerciseDTO(null)),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            USER_JOHN_ID,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(post(TRAINING_PLAN_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Name must not be blank"));

        // make sure user has still no training plans
        mockMvc.perform(get(TRAINING_PLAN_CONTROLLER_BASE_URL)
                .param("userId", newTrainingPlanDTO.userId().toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void givenInvalidTrainingPlanWrongUserId_whenAdminTryToCreateTrainingPlan_thenReturnNotFound() throws Exception {
        UUID invalidUserId = UUID.randomUUID();
        NewTrainingPlanDTO invalidTrainingPlanDTO = new NewTrainingPlanDTO(
            TRAINING_PLAN_NAME,
            List.of(getSquadNewCustomExerciseDTO(null)),
            TRAINING_PLAN_SHORT_DESCRIPTION,
            TRAINING_PLAN_LONG_DESCRIPTION,
            invalidUserId,
            TRAINING_PLAN_TIP
        );

        mockMvc.perform(post(TRAINING_PLAN_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTrainingPlanDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + invalidUserId + " not found"));
    }
}
