package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DELETETrainingSessionIT extends TrainingSessionControllerBase {

    @Test
    void givenTrainingSessionId_whenUserTryToDeleteTrainingSession_thenReturnNoContent() throws Exception {
        assertTrue("Check if training session exists", trainingSessionRepository.findById(TRAINING_SESSION_ID).isPresent());

        // delete training session
        mockMvc.perform(delete(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertTrue("Check if training session is deleted properly", trainingSessionRepository.findById(TRAINING_SESSION_ID).isEmpty());
    }

    @Test
    void givenTrainingSessionId_whenAdminTryToDeleteTrainingSession_thenReturnNoContent() throws Exception {
        assertTrue("Check if training session exists", trainingSessionRepository.findById(TRAINING_SESSION_ID).isPresent());

        // delete training session
        mockMvc.perform(delete(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        assertTrue("Check if training session is deleted properly", trainingSessionRepository.findById(TRAINING_SESSION_ID).isEmpty());
    }

    @Test
    void givenTrainingSessionId_whenUserTryToDeleteTrainingSessionFromOtherUser_thenReturnForbidden() throws Exception {
        // delete training session
        mockMvc.perform(delete(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJaneJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenUnknownTrainingSessionId_whenUserTryToDeleteTrainingSession_thenReturnForbidden() throws Exception {
        UUID UNKNOWN_TRAINING_SESSION_ID = UUID.randomUUID();
        mockMvc.perform(delete(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + UNKNOWN_TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }
}
