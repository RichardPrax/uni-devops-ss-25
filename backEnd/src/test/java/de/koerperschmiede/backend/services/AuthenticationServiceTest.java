package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.config.JwtService;
import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.models.dto.in.AuthenticationRequest;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.dto.out.AuthenticationResponse;
import de.koerperschmiede.backend.models.entities.TokenEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TokenRepository;
import de.koerperschmiede.backend.repositories.UserRepository;
import de.koerperschmiede.backend.util.Role;
import de.koerperschmiede.backend.util.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    private static final String EMAIL_JOHN_DOE = "john.doe@example.com";
    private static final String EMAIL_ADMIN = "admin@example.com";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String JWT_TOKEN = "jwtToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID ANOTHER_USER_ID = UUID.randomUUID();
    private static final LocalDate BIRTHDATE = LocalDate.now();

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenNewUserDTO_whenRegister_thenReturnAuthenticationResponse() {
        NewUserDTO newUserDTO = new NewUserDTO("John", "Doe", BIRTHDATE, PASSWORD, EMAIL_JOHN_DOE);
        UserEntity user = UserEntity.builder()
            .firstName(newUserDTO.firstName())
            .lastName(newUserDTO.lastName())
            .email(newUserDTO.email())
            .password(ENCODED_PASSWORD)
            .role(Role.USER)
            .birthdate(newUserDTO.birthdate())
            .build();

        when(passwordEncoder.encode(newUserDTO.password())).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(JWT_TOKEN);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(REFRESH_TOKEN);

        AuthenticationResponse response = authenticationService.register(newUserDTO);

        assertNotNull(response);
        assertEquals(user.getId().toString(), response.userId());
        assertEquals(JWT_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        assertEquals(Role.USER.toString(), response.role());

        verify(userRepository, times(1)).save(any(UserEntity.class));
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));
    }

    @Test
    void givenAuthenticationRequest_whenAuthenticate_thenReturnAuthenticationResponse() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL_JOHN_DOE, PASSWORD);
        UserEntity user = UserEntity.builder()
            .email(request.email())
            .password(ENCODED_PASSWORD)
            .role(Role.USER)
            .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(JWT_TOKEN);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(REFRESH_TOKEN);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertEquals(user.getId().toString(), response.userId());
        assertEquals(JWT_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
        assertEquals(Role.USER.toString(), response.role());

        verify(authenticationManager, times(1)).authenticate(any());
        verify(tokenRepository, times(1)).save(any(TokenEntity.class));
    }

    @Test
    void givenAuthenticationRequestWithUnknownEmail_whenAuthenticate_thenThrowError() {
        AuthenticationRequest request = new AuthenticationRequest("unknown@gmail.com", PASSWORD);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void givenUserIdAndRequest_whenVerifyUserAccess_thenDoNotThrowException() {
        UserEntity user = UserEntity.builder()
            .email(EMAIL_JOHN_DOE)
            .role(Role.USER)
            .build();
        user.setId(USER_ID);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertDoesNotThrow(() -> authenticationService.verifyUserAccess(USER_ID));
    }

    @Test
    void givenDifferentUserIdAndRequest_whenVerifyUserAccess_thenThrowAccessException() {
        UserEntity user = UserEntity.builder()
            .email(EMAIL_JOHN_DOE)
            .role(Role.USER)
            .build();
        user.setId(ANOTHER_USER_ID);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AccessException exception = assertThrows(AccessException.class, () -> authenticationService.verifyUserAccess(USER_ID));
        assertEquals("You do not have permission to access this resource", exception.getMessage());
    }

    @Test
    void givenAdminRequest_whenVerifyUserAccessForAnotherUser_thenDoNotThrowException() {
        UserEntity adminUser = UserEntity.builder()
            .email(EMAIL_ADMIN)
            .role(Role.ADMIN)
            .build();
        adminUser.setId(USER_ID);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(adminUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertDoesNotThrow(() -> authenticationService.verifyUserAccess(ANOTHER_USER_ID));
    }

    @Test
    void givenNotAuthenticatedUser_whenVerifyUserAccess_thenThrowAccessException() {
        UserEntity user = UserEntity.builder()
            .email(EMAIL_JOHN_DOE)
            .role(Role.USER)
            .build();
        user.setId(ANOTHER_USER_ID);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        AccessException exception = assertThrows(AccessException.class, () -> authenticationService.verifyUserAccess(USER_ID));
        assertEquals("User is not authenticated", exception.getMessage());
    }

    @Test
    void givenUser_whenAuthenticate_thenRevokeAllUserTokens() {
        AuthenticationRequest request = new AuthenticationRequest(EMAIL_JOHN_DOE, PASSWORD);
        UserEntity user = UserEntity.builder()
            .email(request.email())
            .password(ENCODED_PASSWORD)
            .role(Role.USER)
            .build();

        TokenEntity activeToken = TokenEntity.builder()
            .user(user)
            .token("activeToken")
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(JWT_TOKEN);
        when(jwtService.generateRefreshToken(any(UserEntity.class))).thenReturn(REFRESH_TOKEN);
        when(tokenRepository.findAllValidTokenByUser(user.getId())).thenReturn(List.of(activeToken));

        authenticationService.authenticate(request);

        assertTrue(activeToken.isExpired());
        assertTrue(activeToken.isRevoked());
        verify(tokenRepository, times(1)).saveAll(anyList());
    }
}
