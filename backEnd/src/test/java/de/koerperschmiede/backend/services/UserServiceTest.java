package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.exceptions.AlreadyExistsException;
import de.koerperschmiede.backend.models.dto.in.NewUserDTO;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final String EMAIL = "test@example.com";
    private static final String FIRST_NAME = "John";
    private static final String LAST_NAME = "Doe";
    private static final String PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final LocalDate BIRTH_DATE = LocalDate.now();

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenUsersExist_whenFindAll_thenReturnAllUsers() {
        List<UserEntity> users = List.of(new UserEntity(), new UserEntity());
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.findAll();

        assertEquals(users.size(), result.size());
        verify(userRepository).findAll();
    }

    @Test
    void givenUserIdExists_whenFindById_thenReturnUser() {
        UserEntity user = new UserEntity();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findById(USER_ID);

        assertTrue(result.isPresent());
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void givenUserIdDoesNotExist_whenFindById_thenReturnEmptyOptional() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findById(USER_ID);

        assertTrue(result.isEmpty());
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void givenEmailExists_whenFindByEmail_thenReturnUser() {
        UserEntity user = new UserEntity();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        Optional<UserEntity> result = userService.findByEmail(EMAIL);

        assertTrue(result.isPresent());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void givenEmailDoesNotExist_whenFindByEmail_thenReturnEmptyOptional() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        Optional<UserEntity> result = userService.findByEmail(EMAIL);

        assertTrue(result.isEmpty());
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    void givenValidUserIdAndNewUserDTO_whenUpdate_thenReturnUpdatedUser() {
        NewUserDTO newUserDTO = new NewUserDTO(FIRST_NAME, LAST_NAME, BIRTH_DATE, PASSWORD, EMAIL);
        UserEntity user = new UserEntity();
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(user)).thenReturn(user);

        UserEntity result = userService.update(USER_ID, newUserDTO);

        assertNotNull(result);
        assertEquals(FIRST_NAME, user.getFirstName());
        assertEquals(LAST_NAME, user.getLastName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(BIRTH_DATE, user.getBirthdate());
        verify(userRepository).findById(USER_ID);
        verify(userRepository).save(user);
    }

    @Test
    void givenInvalidUserId_whenUpdate_thenThrowException() {
        NewUserDTO newUserDTO = new NewUserDTO(FIRST_NAME, LAST_NAME, BIRTH_DATE, PASSWORD, EMAIL);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> userService.update(USER_ID, newUserDTO));

        assertEquals("User with id: " + USER_ID + " not found", exception.getMessage());
        verify(userRepository).findById(USER_ID);
    }

    @Test
    void givenEmailAlreadyExists_whenUpdate_thenThrowException() {
        NewUserDTO newUserDTO = new NewUserDTO(FIRST_NAME, LAST_NAME, BIRTH_DATE, PASSWORD, EMAIL);
        UserEntity existingUser = new UserEntity();
        existingUser.setId(UUID.randomUUID());
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(new UserEntity()));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(existingUser));

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class, () -> userService.update(USER_ID, newUserDTO));

        assertEquals("Email: " + EMAIL + " is already in use", exception.getMessage());
        verify(userRepository).findById(USER_ID);
        verify(userRepository).findByEmail(EMAIL);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void givenUserId_whenDeleteById_thenVerifyDeletion() {
        userService.deleteById(USER_ID);
        verify(userRepository).deleteById(USER_ID);
    }
}
