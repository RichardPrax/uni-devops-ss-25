package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETTrainingSessionsIT extends TrainingSessionControllerBase {

    @Test
    void givenEmptyDatabase_whenUserTryToGetTrainingSessionsForHimSelf_thenReturnEmptyList() throws Exception {
        trainingSessionRepository.deleteAll();

        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", USER_JOHN_ID.toString())
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void givenDatabaseWithTrainingSessions_whenUserTryToGetTrainingSessionsForHimSelf_thenReturnListOfTrainingSessions() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", USER_JOHN_ID.toString())
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0]['id']").value(TRAINING_SESSION_ID.toString()))
            .andExpect(jsonPath("$[0]['notes']").value(TRAINING_SESSION_NOTES))
            .andExpect(jsonPath("$[0]['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$[0]['trainingPlan']['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$[0]['trainingPlan']['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises']").isArray())
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['tip']").value(SQUAT_TIP));
    }

    @Test
    void givenDatabaseWithTrainingSessions_whenUserTryToGetTrainingSessionsForOtherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", USER_JANE_ID.toString())
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenDatabaseWithTrainingSessions_whenAdminTryToGetTrainingSessionsForOtherUser_thenReturnListOfTrainingSessions() throws Exception {
        mockMvc.perform(get(TRAINING_SESSION_CONTROLLER_BASE_URL)
                .param("userId", USER_JOHN_ID.toString())
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0]['id']").value(TRAINING_SESSION_ID.toString()))
            .andExpect(jsonPath("$[0]['notes']").value(TRAINING_SESSION_NOTES))
            .andExpect(jsonPath("$[0]['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['shortDescription']").value(TRAINING_PLAN_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$[0]['trainingPlan']['longDescription']").value(TRAINING_PLAN_LONG_DESCRIPTION))
            .andExpect(jsonPath("$[0]['trainingPlan']['tip']").value(TRAINING_PLAN_TIP))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises']").isArray())
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['id']").value(SQUAT_ID_GENERAL.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['name']").value(SQUAT_NAME))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['categories']").value("LOWER_BODY"))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['equipment']").value("CHAIR"))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['shortDescription']").value(SQUAT_DESCRIPTION_SHORT))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['longDescription']").value(SQUAT_DESCRIPTION_LONG))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['directions']").value(SQUAT_DIRECTIONS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['video']").value(SQUAT_VIDEO))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['generalExercise']['thumbnailUrl']").value(SQUAT_THUMBNAIL_URL))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['repetitions']").value(SQUAT_REPS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['sets']").value(SQUAT_SETS))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['id']").value(SQUAT_ID_CUSTOM.toString()))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['durationInMinutes']").value(SQUAT_DURATION_IN_MINUTES))
            .andExpect(jsonPath("$[0]['trainingPlan']['exercises'][0]['tip']").value(SQUAT_TIP));
    }
}
