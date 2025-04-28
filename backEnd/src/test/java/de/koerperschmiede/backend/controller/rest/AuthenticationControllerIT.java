package de.koerperschmiede.backend.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.koerperschmiede.backend.models.dto.in.AuthenticationRequest;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.UserRepository;
import de.koerperschmiede.backend.util.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class does not inherit from BaseIT, because
 * we do not need any predefined data in the database.
 */

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIT {

    private static final String BASE_URL = "/api/v1/auth";
    private static final String REGISTER_URL = BASE_URL + "/register";
    private static final String AUTHENTICATE_URL = BASE_URL + "/authenticate";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String EMAIL = "john.doe@example.com";
    private static final String PASSWORD = "Test1234!";
    private static final String INVALID_EMAIL = "verynice@gmail.com";
    private static final String INVALID_NAME = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll(); // Clear the database after each test
    }

    @Test
    void givenNewUserDTO_whenRegister_thenReturnCreated() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, PASSWORD);

        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUserDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void givenExistingUserEmail_whenRegister_thenReturnConflict() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, PASSWORD);
        UserEntity user = createUserEntity(newUserDTO);
        userRepository.save(user);

        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUserDTO)))
            .andDo(print())
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.reason").value("User with provided email: " + EMAIL + " already exists"));
    }

    @Test
    void givenInvalidUserObjectBlankEmail_whenRegister_thenReturnBadRequest() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(INVALID_NAME, PASSWORD);

        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUserDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Email must not be blank"));
    }

    @Test
    void givenInvalidUserObjectNullPassword_whenRegister_thenReturnBadRequest() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, null);

        mockMvc.perform(post(REGISTER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUserDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Password must not be null"));
    }

    @Test
    void givenExistingUserEmail_whenAuthenticate_thenReturnOk() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, PASSWORD);
        UserEntity user = createUserEntity(newUserDTO);
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthenticationRequest authRequest = new AuthenticationRequest(EMAIL, PASSWORD);

        mockMvc.perform(post(AUTHENTICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void givenExistingUserEmail_whenAuthenticateWithWrongEmail_thenReturnForbidden() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, PASSWORD);
        UserEntity user = createUserEntity(newUserDTO);
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthenticationRequest authRequest = new AuthenticationRequest(INVALID_EMAIL, PASSWORD);

        mockMvc.perform(post(AUTHENTICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenExistingUserEmail_whenAuthenticateWithWrongPassword_thenReturnForbidden() throws Exception {
        NewUserDTO newUserDTO = createNewUserDTO(EMAIL, PASSWORD);
        UserEntity user = createUserEntity(newUserDTO);
        user.setRole(Role.USER);
        userRepository.save(user);

        AuthenticationRequest authRequest = new AuthenticationRequest(EMAIL, "wrongPassword");

        mockMvc.perform(post(AUTHENTICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    void givenEmptyDB_whenAuthenticateWithBlankEmailInAuthenticationRequestObject_thenReturnBadRequest() throws Exception {
        AuthenticationRequest authRequest = new AuthenticationRequest(INVALID_NAME, PASSWORD);

        mockMvc.perform(post(AUTHENTICATE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(authRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.reason").value("Email must not be blank"));
    }

    private NewUserDTO createNewUserDTO(String email, String password) {
        return new NewUserDTO(FIRST_NAME, LAST_NAME, LocalDate.now(), password, email);
    }

    private UserEntity createUserEntity(NewUserDTO newUserDTO) {
        return new UserEntity(
            newUserDTO.firstName(),
            newUserDTO.lastName(),
            newUserDTO.email(),
            passwordEncoder.encode(newUserDTO.password()),
            newUserDTO.birthdate()
        );
    }
}
