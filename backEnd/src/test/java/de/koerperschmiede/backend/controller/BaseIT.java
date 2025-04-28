package de.koerperschmiede.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.koerperschmiede.backend.config.JwtService;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.entities.TokenEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.*;
import de.koerperschmiede.backend.util.Role;
import de.koerperschmiede.backend.util.TokenType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static de.koerperschmiede.backend.controller.Constants.*;

/**
 * Base class for integration tests.
 * Defines a base configuration for all integration tests.
 * Created a user with the role of an admin.
 * Created a user with the role of a user.
 * Gets the JWT token for both users to test secured endpoints.
 */

@SpringBootTest
@AutoConfigureMockMvc
public class BaseIT {


    protected String userJohnJwtTokenWithoutPrefix;
    protected String userJaneJwtTokenWithoutPrefix;
    protected String userAdminJwtTokenWithoutPrefix;

    protected String userJohnJwtToken;
    protected String userJaneJwtToken;
    protected String userAdminJwtToken;

    protected UserEntity john;
    protected UserEntity jane;
    protected UserEntity admin;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    protected GeneralExerciseRepository generalExerciseRepository;

    @Autowired
    protected CustomExerciseRepository customExerciseRepository;

    @Autowired
    protected TrainingPlanRepository trainingPlanRepository;

    @Autowired
    protected TrainingSessionRepository trainingSessionRepository;

    @Autowired
    protected JwtService jwtService;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected ObjectMapper mapper;

    // do not rename this method to setUp, otherwise the test will fail because the children override the method
    @BeforeEach
    void setUpBase() {
        john = createAndSaveUser(USER_JOHN_ID, EMAIL_JOHN, FIRST_NAME_JOHN, LAST_NAME_DOE, Role.USER);
        jane = createAndSaveUser(USER_JANE_ID, EMAIL_JANE, FIRST_NAME_JANE, LAST_NAME_DOE, Role.USER);
        admin = createAndSaveUser(USER_ADMIN_ID, EMAIL_ADMIN, FIRST_NAME_ADMIN, LAST_NAME_DOE, Role.ADMIN);

        userJohnJwtTokenWithoutPrefix = jwtService.generateToken(john);
        userJaneJwtTokenWithoutPrefix = jwtService.generateToken(jane);
        userAdminJwtTokenWithoutPrefix = jwtService.generateToken(admin);

        saveUserToken(john, userJohnJwtTokenWithoutPrefix);
        saveUserToken(jane, userJaneJwtTokenWithoutPrefix);
        saveUserToken(admin, userAdminJwtTokenWithoutPrefix);

        userJohnJwtToken = JWT_TOKEN_PREFIX + userJohnJwtTokenWithoutPrefix;
        userJaneJwtToken = JWT_TOKEN_PREFIX + userJaneJwtTokenWithoutPrefix;
        userAdminJwtToken = JWT_TOKEN_PREFIX + userAdminJwtTokenWithoutPrefix;

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    // clear up the complete database after each test
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        generalExerciseRepository.deleteAll();
        customExerciseRepository.deleteAll();
        trainingPlanRepository.deleteAll();
        trainingSessionRepository.deleteAll();
    }

    private UserEntity createAndSaveUser(UUID id, String email, String firstName, String lastName, Role role) {
        NewUserDTO newUserDTO = new NewUserDTO(firstName, lastName, LocalDate.now(), PASSWORD, email);
        UserEntity user = createUserEntity(newUserDTO);
        user.setRole(role);
        user.setId(id);
        return userRepository.save(user);
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

    private void saveUserToken(UserEntity user, String jwtToken) {
        TokenEntity token = TokenEntity.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
        tokenRepository.save(token);
    }
}
