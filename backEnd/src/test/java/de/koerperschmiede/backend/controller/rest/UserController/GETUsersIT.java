package de.koerperschmiede.backend.controller.rest.UserController;

import de.koerperschmiede.backend.controller.BaseIT;
import org.junit.jupiter.api.Test;

import static de.koerperschmiede.backend.controller.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GETUsersIT extends BaseIT {
    @Test
    void givenExistingUsers_whenUserFindAll_thenReturnForbidden() throws Exception {
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userJohnJwtToken))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenExistingUsers_whenAdminFindAll_thenReturnListOfUsers() throws Exception {
        mockMvc.perform(get(USER_CONTROLLER_BASE_URL)
                .header(AUTH_HEADER, userAdminJwtToken))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(3))
            .andExpect(jsonPath("$[0]['firstName']").value(FIRST_NAME_JOHN))
            .andExpect(jsonPath("$[1]['firstName']").value(FIRST_NAME_JANE))
            .andExpect(jsonPath("$[2]['firstName']").value(FIRST_NAME_ADMIN));
    }
}
