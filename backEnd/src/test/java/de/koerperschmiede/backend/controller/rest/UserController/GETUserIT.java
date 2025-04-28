package de.koerperschmiede.backend.controller.rest.UserController;

import de.koerperschmiede.backend.controller.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETUserIT extends BaseIT {

    @Test
    void givenExistingUser_whenTryToAccessOwnData_thenReturnData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$.email").value(EMAIL_JOHN));
    }

    @Test
    void givenExistingUser_whenTryToAccessOtherData_thenReturnForbidden() throws Exception {
        UUID idFromOtherUser = USER_JANE_ID;
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_BASE_URL + "/" + idFromOtherUser)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenExistingAdmin_whenTryToAccessOtherData_thenReturnData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_BASE_URL + "/" + USER_JOHN_ID)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$.email").value(EMAIL_JOHN));
    }

    @Test
    void givenExistingAdmin_whenTryToAccessNotRegisteredUser_thenReturnNotFound() throws Exception {
        UUID randomUser = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_BASE_URL + "/" + randomUser)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with id: " + randomUser + " not found"));
    }

    @Test
    void givenExistingUser_whenFindByEmail_thenReturnUserData() throws Exception {
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/email")
                .param("email", EMAIL_JOHN)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$.email").value(EMAIL_JOHN));
    }

    @Test
    void givenExistingUser_whenTryToAccessOtherEmail_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/email")
                .param("email", EMAIL_JANE)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.reason").value("You do not have permission to access this resource"));
    }

    @Test
    void givenExistingAdmin_whenFindByEmail_thenReturnUserData() throws Exception {
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/email")
                .param("email", EMAIL_JOHN)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$.email").value(EMAIL_JOHN));
    }

    @Test
    void givenExistingAdmin_whenFindByEmailWithUnknownEmail_thenReturnNotFound() throws Exception {
        String randomMail = "hello@random.com";
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL + "/email")
                .param("email", randomMail)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.reason").value("User with email: " + randomMail + "not found"));
    }
}
