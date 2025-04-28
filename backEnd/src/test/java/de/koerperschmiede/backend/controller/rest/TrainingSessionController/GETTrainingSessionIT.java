package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETTrainingSessionIT extends TrainingSessionControllerBase {

    @Test
    void givenEmptyDatabase_whenUserTryToGetTrainingSessionForHimSelf_thenReturnForbidden() throws Exception {
        trainingSessionRepository.deleteAll();

        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenDatabaseWithTrainingSession_whenUserTryToGetTrainingSessionForHimSelf_thenReturnTrainingSession() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_SESSION_ID.toString()))
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
    void givenDatabaseWithTrainingSession_whenUserTryToGetTrainingSessionForOtherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJaneJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenDatabaseWithTrainingSession_whenAdminTryToGetTrainingSessionForOtherUser_thenReturnTrainingSession() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_SESSION_ID.toString()))
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
    void givenEmptyDatabase_whenAdminTryToGetTrainingSession_thenReturnNotFound() throws Exception {
        trainingSessionRepository.deleteAll();

        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training session with id: " + TRAINING_SESSION_ID + " not found"));
    }

}
