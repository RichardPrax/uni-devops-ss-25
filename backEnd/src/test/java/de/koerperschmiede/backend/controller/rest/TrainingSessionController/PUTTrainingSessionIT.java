package de.koerperschmiede.backend.controller.rest.TrainingSessionController;

import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PUTTrainingSessionIT extends TrainingSessionControllerBase {

    @Test
    void givenValidTrainingSession_whenUserUpdatesTrainingSession_thenReturnUpdatedTrainingSession() throws Exception {
        String newNotes = "Updated notes";

        NewTrainingSessionDTO updatedTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            TRAINING_SESSION_DATE,
            newNotes
        );

        // check only date and notes, because training plan and user id are not updated
        mockMvc.perform(put(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_SESSION_ID.toString()))
            .andExpect(jsonPath("$['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$['notes']").value(newNotes));
    }

    @Test
    void givenInvalidTrainingSession_whenUserUpdatesTrainingSession_thenReturnBadRequest() throws Exception {
        Instant invalidDate = null;

        NewTrainingSessionDTO updatedTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            invalidDate,
            TRAINING_SESSION_NOTES
        );

        mockMvc.perform(put(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Date must not be null"));
    }

    @Test
    void givenValidTrainingSession_whenUserUpdatesTrainingSessionOfOtherUser_thenReturnForbidden() throws Exception {
        String newNotes = "Updated notes";

        NewTrainingSessionDTO updatedTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            TRAINING_SESSION_DATE,
            newNotes
        );

        mockMvc.perform(put(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userJaneJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenValidTrainingSession_whenAdminUpdatesTrainingSessionOfOtherUser_thenReturnUpdatedTrainingSession() throws Exception {
        String newNotes = "Updated notes";

        NewTrainingSessionDTO updatedTrainingSessionDTO = new NewTrainingSessionDTO(
            TRAINING_PLAN_ID,
            USER_JOHN_ID,
            TRAINING_SESSION_DATE,
            newNotes
        );

        // check only date and notes, because training plan and user id are not updated
        mockMvc.perform(put(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + TRAINING_SESSION_ID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['id']").value(TRAINING_SESSION_ID.toString()))
            .andExpect(jsonPath("$['date']").value(TRAINING_SESSION_DATE.toString()))
            .andExpect(jsonPath("$['notes']").value(newNotes));
    }

    @Test
    void givenInvalidSessionId_whenAdminTryToUpdateTrainingSession_thenReturnNotFound() throws Exception {
        UUID sessionId = UUID.randomUUID();

        mockMvc.perform(put(TRAINING_SESSION_CONTROLLER_BASE_URL + "/" + sessionId)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newTrainingSessionDTO)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("Training session with id: " + sessionId + " not found"));
    }
}
