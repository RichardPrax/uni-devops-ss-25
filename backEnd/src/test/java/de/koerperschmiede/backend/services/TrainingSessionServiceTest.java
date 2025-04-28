package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.TrainingSessionEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TrainingSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingSessionServiceTest {

    private static final UUID TRAINING_SESSION_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID TRAINING_PLAN_ID = UUID.randomUUID();
    private static final String TRAINING_SESSION_NOTES = "Test Notes";

    @Mock
    private TrainingSessionRepository trainingSessionRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingPlanService trainingPlanService;

    @InjectMocks
    private TrainingSessionService trainingSessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenTrainingSessionsExist_whenFindAllByUserId_thenReturnAllTrainingSessions() {
        List<TrainingSessionEntity> trainingSessions = List.of(new TrainingSessionEntity(), new TrainingSessionEntity());
        when(trainingSessionRepository.findAllByUserId(USER_ID)).thenReturn(trainingSessions);

        List<TrainingSessionEntity> result = trainingSessionService.findAllByUserId(USER_ID);

        assertEquals(trainingSessions.size(), result.size());
        verify(trainingSessionRepository).findAllByUserId(USER_ID);
    }

    @Test
    void givenValidTrainingSessionId_whenFindById_thenReturnTrainingSession() {
        TrainingSessionEntity trainingSession = new TrainingSessionEntity();
        when(trainingSessionRepository.findById(TRAINING_SESSION_ID)).thenReturn(Optional.of(trainingSession));

        Optional<TrainingSessionEntity> result = trainingSessionService.findById(TRAINING_SESSION_ID);

        assertTrue(result.isPresent());
        verify(trainingSessionRepository).findById(TRAINING_SESSION_ID);
    }

    @Test
    void givenInvalidTrainingSessionId_whenFindById_thenReturnEmptyOptional() {
        when(trainingSessionRepository.findById(TRAINING_SESSION_ID)).thenReturn(Optional.empty());

        Optional<TrainingSessionEntity> result = trainingSessionService.findById(TRAINING_SESSION_ID);

        assertTrue(result.isEmpty());
        verify(trainingSessionRepository).findById(TRAINING_SESSION_ID);
    }

    @Test
    void givenNewTrainingSession_whenCreate_thenReturnCreatedTrainingSession() {
        TrainingSessionEntity trainingSession = new TrainingSessionEntity();
        trainingSession.setNotes(TRAINING_SESSION_NOTES);
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);

        UserEntity user = new UserEntity();
        user.setId(USER_ID);

        TrainingPlanEntity trainingPlan = new TrainingPlanEntity(
            "name",
            new ArrayList<>(),
            user,
            "short",
            "long",
            "tip"
        );

        when(trainingSessionRepository.save(any(TrainingSessionEntity.class))).thenReturn(trainingSession);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(user));
        when(trainingPlanService.findById(TRAINING_PLAN_ID)).thenReturn(Optional.of(trainingPlan));

        TrainingSessionEntity result = trainingSessionService.create(newTrainingSessionDTO);

        assertNotNull(result);
        assertEquals(TRAINING_SESSION_NOTES, result.getNotes());
        verify(trainingSessionRepository).save(any(TrainingSessionEntity.class));
    }

    @Test
    void givenInvalidUserId_whenCreate_thenThrowNoSuchElementException() {
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        when(userService.findById(USER_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingSessionService.create(newTrainingSessionDTO));

        assertEquals("User with id: " + USER_ID + " not found", exception.getMessage());
        verify(userService).findById(USER_ID);
    }

    @Test
    void givenInvalidTrainingPlanId_whenCreate_thenThrowNoSuchElementException() {
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        UserEntity user = new UserEntity();
        user.setId(USER_ID);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(user));
        when(trainingPlanService.findById(TRAINING_PLAN_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingSessionService.create(newTrainingSessionDTO));

        assertEquals("Training plan with id: " + TRAINING_PLAN_ID + " not found", exception.getMessage());
        verify(userService).findById(USER_ID);
        verify(trainingPlanService).findById(TRAINING_PLAN_ID);
    }

    @Test
    void givenMismatchedUserId_whenCreate_thenThrowIllegalArgumentException() {
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        UserEntity user = new UserEntity();
        user.setId(USER_ID);
        UserEntity differentUser = new UserEntity();
        differentUser.setId(UUID.randomUUID());
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        trainingPlan.setUser(differentUser);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(user));
        when(trainingPlanService.findById(TRAINING_PLAN_ID)).thenReturn(Optional.of(trainingPlan));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainingSessionService.create(newTrainingSessionDTO));

        assertEquals("User ID: " + USER_ID + " does not match the user ID: " + differentUser.getId() + " of the training plan", exception.getMessage());
        verify(userService).findById(USER_ID);
        verify(trainingPlanService).findById(TRAINING_PLAN_ID);
    }

    @Test
    void givenValidTrainingSessionIdAndUpdatedTrainingSession_whenUpdate_thenReturnUpdatedTrainingSession() {
        TrainingSessionEntity trainingSession = new TrainingSessionEntity();
        trainingSession.setNotes(TRAINING_SESSION_NOTES);
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        when(trainingSessionRepository.findById(TRAINING_SESSION_ID)).thenReturn(Optional.of(trainingSession));
        when(trainingSessionRepository.save(trainingSession)).thenReturn(trainingSession);

        TrainingSessionEntity result = trainingSessionService.update(TRAINING_SESSION_ID, newTrainingSessionDTO);

        assertNotNull(result);
        assertEquals(TRAINING_SESSION_NOTES, result.getNotes());
        verify(trainingSessionRepository).findById(TRAINING_SESSION_ID);
        verify(trainingSessionRepository).save(trainingSession);
    }

    @Test
    void givenNonExistentTrainingSessionId_whenUpdate_thenThrowNoSuchElementException() {
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(TRAINING_PLAN_ID, USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        when(trainingSessionRepository.findById(TRAINING_SESSION_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingSessionService.update(TRAINING_SESSION_ID, newTrainingSessionDTO));

        assertEquals("Training session with id: " + TRAINING_SESSION_ID + " not found", exception.getMessage());
        verify(trainingSessionRepository).findById(TRAINING_SESSION_ID);
    }

    @Test
    void givenInvalidTrainingSessionId_whenUpdate_thenThrowException() {
        NewTrainingSessionDTO newTrainingSessionDTO = new NewTrainingSessionDTO(UUID.randomUUID(), USER_ID, Instant.now(), TRAINING_SESSION_NOTES);
        when(trainingSessionRepository.findById(TRAINING_SESSION_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingSessionService.update(TRAINING_SESSION_ID, newTrainingSessionDTO));

        assertEquals("Training session with id: " + TRAINING_SESSION_ID + " not found", exception.getMessage());
        verify(trainingSessionRepository).findById(TRAINING_SESSION_ID);
    }

    @Test
    void givenTrainingSessionId_whenDeleteById_thenVerifyDeletion() {
        trainingSessionService.deleteById(TRAINING_SESSION_ID);
        verify(trainingSessionRepository).deleteById(TRAINING_SESSION_ID);
    }
}
