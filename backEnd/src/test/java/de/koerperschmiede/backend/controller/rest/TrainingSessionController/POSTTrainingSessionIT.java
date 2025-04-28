package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class POSTTrainingSessionIT extends TrainingSessionControllerBase {

    @BeforeEach
    void setUpSpecific() {
        trainingSessionRepository.deleteAll();
    }

    @Test
    void givenValidTrainingSession_whenUserTryToCreateTrainingSessionForHimSelf_thenReturnCreatedTrainingSession() throws Exception {
        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$['notes']").value(TRAINING_SESSION_NOTES))
            .andExpect(jsonPath("$['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$['trainingPlan']['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$['trainingPlan']['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$['trainingPlan']['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$['trainingPlan']['exercises']").isArray())
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenValidTrainingSession_whenUserTryToCreateTrainingSessionForAnotherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJaneJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));

        // make sure user has still no training sessions
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", newTrainingSessionDTO.userId().toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void givenValidTrainingSession_whenAdminTryToCreateTrainingSessionForAnotherUser_thenReturnCreatedTrainingSession() throws Exception {
        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$['notes']").value(TRAINING_SESSION_NOTES))
            .andExpect(jsonPath("$['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$['trainingPlan']['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$['trainingPlan']['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$['trainingPlan']['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$['trainingPlan']['exercises']").isArray())
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$['trainingPlan']['exercises'][0]['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenInvalidTrainingSession_whenUserTryToCreateTrainingSessionForHimSelf_thenReturnBadRequest() throws Exception {
        // create new training session with invalid date
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            null,
            TRAINING_SESSION_NOTES
        );

        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Date must not be null"));

        // make sure user has still no training sessions
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", newTrainingSessionDTO.userId().toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void givenInvalidTrainingPlanId_whenAdminTryToCreateTrainingSessionForUser_thenReturnNotFound() throws Exception {
        UUID trainingPlanId = UUID.randomUUID();
        // create new training session with invalid training plan id
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(
            trainingPlanId,
            USER_JOHN_ID,
            TRAINING_SESSION_DATE,
            TRAINING_SESSION_NOTES
        );

        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training plan with id: " + trainingPlanId + " not found"));
    }

    @Test
    void givenInvalidUserId_whenAdminTryToCreateTrainingSessionForUser_thenReturnNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        // create new training session with invalid user id
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            userId,
            TRAINING_SESSION_DATE,
            TRAINING_SESSION_NOTES
        );

        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + userId + " not found"));
    }

    @Test
    void givenUserIdDoesNotMatchTrainingPlanUserId_whenAdminTryToCreateTrainingSessionForUser_thenReturnBadRequest() throws Exception {
        // create new training session with invalid user id
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JANE_ID,
            TRAINING_SESSION_DATE,
            TRAINING_SESSION_NOTES
        );

        mockMvc.perform(post(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("User ID: " + USER_JANE_ID + " does not match the user ID: " + USER_JOHN_ID + " of the training plan"));
    }
}
