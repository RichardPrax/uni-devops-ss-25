package de.koerperschmiede.backend.controller.rest.UserController;

import de.koerperschmiede.backend.controller.BaseIT;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DELETEUserIT extends BaseIT {
    @Test
    void givenExistingUsersAndAdmin_whenAdminTryToDeleteAnUser_thenDeleteUserAndReturnNoContent() throws Exception {

        // make sure user john is not deleted
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$.email").value(EMAIL_JOHN));

        mockMvc.perform(delete(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());

        // make sure user john deleted
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + USER_JOHN_ID + " not found"));
    }

    @Test
    void givenAdmin_whenTryToDeleteUserThatNotExists_thenDoNothingAndReturnNoContent() throws Exception {

        // random user that does not exist
        UUID randomUser = UUID.randomUUID();
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/" + randomUser)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + randomUser + " not found"));

        mockMvc.perform(delete(USER_CONTROLLER_BASE_URL + "/" + randomUser)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    void givenExistingUser_whenTryToDeleteAnyUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete(USER_CONTROLLER_BASE_URL + "/" + USER_JANE_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("Access Denied"));
    }
}
