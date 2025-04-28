package de.koerperschmiede.backend.controller.rest.UserController;

import de.koerperschmiede.backend.controller.BaseIT;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PUTUserIT extends BaseIT {

    @Test
    void givenExistingUser_whenUpdateOwnData_thenReturnUpdatedData() throws Exception {
        NewUserDTO updatedUser = new NewUserDTO("NewJohn", LAST_NAME_DOE, LocalDate.now(), PASSWORD, "new.john.doe@example.com");

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(updatedUser.email()))
            .andExpect(jsonPath("$.firstName").value(updatedUser.firstName()));
    }

    @Test
    void givenExistingUser_whenTryToUpdateOtherUser_thenReturnForbidden() throws Exception {
        NewUserDTO updatedUser = new NewUserDTO(FIRST_NAME_JANE, LAST_NAME_DOE, LocalDate.now(), PASSWORD, "new.jane.doe@example.com");

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + USER_JANE_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenAdmin_whenUpdateOtherUser_thenReturnUpdatedData() throws Exception {
        NewUserDTO updatedUser = new NewUserDTO("NewJane", LAST_NAME_DOE, LocalDate.now(), PASSWORD, "new.jane.doe@example.com");

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + USER_JANE_ID)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(updatedUser.email()))
            .andExpect(jsonPath("$.firstName").value(updatedUser.firstName()));
    }

    @Test
    void givenAdmin_whenTryToUpdateNonExistentUser_thenReturnNotFound() throws Exception {
        UUID randomUserId = UUID.randomUUID();
        NewUserDTO updatedUser = new NewUserDTO("NonExistent", LAST_NAME_DOE, LocalDate.now(), PASSWORD, "non.existent@example.com");

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + randomUserId)
                .header(AUTH_HEADER, userAdminJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedUser)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + randomUserId + " not found"));
    }

    @Test
    void givenExistingUser_whenUpdateWithInvalidParameters_thenReturnBadRequest() throws Exception {
        NewUserDTO invalidUser = new NewUserDTO(FIRST_NAME_JOHN, LAST_NAME_DOE, LocalDate.now(), PASSWORD, "");

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidUser)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Email must not be blank"));
    }

    @Test
    void givenExistingUser_whenUpdateWithAlreadyExistingEmail_thenReturnBadRequest() throws Exception {
        NewUserDTO invalidUser = new NewUserDTO(FIRST_NAME_JOHN, LAST_NAME_DOE, LocalDate.now(), PASSWORD, EMAIL_JANE);

        mockMvc.perform(put(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userJohnJwtToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidUser)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.reason").value("Email: " + EMAIL_JANE + " is already in use"));
    }
}
