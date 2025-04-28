package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TrainingPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingPlanServiceTest {

    private static final UUID TRAINING_PLAN_ID = UUID.randomUUID();
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String NAME = "Full Body Workout";
    private static final String SHORT_DESCRIPTION = "A great plan for beginners";
    private static final String LONG_DESCRIPTION = "This plan focuses on all muscle groups";
    private static final String TIP = "Stay hydrated";

    @Mock
    private TrainingPlanRepository trainingPlanRepository;

    @Mock
    private UserService userService;

    @Mock
    private CustomExerciseService customExerciseService;

    @Mock
    private GeneralExerciseService generalExerciseService;

    @InjectMocks
    private TrainingPlanService trainingPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenTrainingPlansExist_whenFindAllByUserId_thenReturnAllTrainingPlans() {
        List<TrainingPlanEntity> trainingPlans = List.of(new TrainingPlanEntity(), new TrainingPlanEntity());
        when(trainingPlanRepository.findAllByUserId(USER_ID)).thenReturn(trainingPlans);

        List<TrainingPlanEntity> result = trainingPlanService.findAllByUserId(USER_ID);

        assertEquals(trainingPlans.size(), result.size());
        verify(trainingPlanRepository).findAllByUserId(USER_ID);
    }

    @Test
    void givenValidTrainingPlanId_whenFindById_thenReturnTrainingPlan() {
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        when(trainingPlanRepository.findById(TRAINING_PLAN_ID)).thenReturn(Optional.of(trainingPlan));

        Optional<TrainingPlanEntity> result = trainingPlanService.findById(TRAINING_PLAN_ID);

        assertTrue(result.isPresent());
        verify(trainingPlanRepository).findById(TRAINING_PLAN_ID);
    }

    @Test
    void givenInvalidTrainingPlanId_whenFindById_thenReturnEmptyOptional() {
        when(trainingPlanRepository.findById(TRAINING_PLAN_ID)).thenReturn(Optional.empty());

        Optional<TrainingPlanEntity> result = trainingPlanService.findById(TRAINING_PLAN_ID);

        assertTrue(result.isEmpty());
        verify(trainingPlanRepository).findById(TRAINING_PLAN_ID);
    }

    @Test
    void givenNewTrainingPlanDTO_whenCreate_thenReturnCreatedTrainingPlan() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(UUID.randomUUID(), 10, 3, 30, TIP, null, null);
        NewTrainingPlanDTO newTrainingPlanDTO = new NewTrainingPlanDTO(NAME, List.of(newCustomExerciseDTO), SHORT_DESCRIPTION, LONG_DESCRIPTION, USER_ID, TIP);
        UserEntity user = new UserEntity();
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        trainingPlan.setUser(user);

        when(userService.findById(USER_ID)).thenReturn(Optional.of(user));
        when(trainingPlanRepository.save(any(TrainingPlanEntity.class))).thenReturn(trainingPlan);

        TrainingPlanEntity result = trainingPlanService.create(newTrainingPlanDTO);

        assertNotNull(result);
        verify(userService).findById(USER_ID);
        verify(trainingPlanRepository).save(any(TrainingPlanEntity.class));
    }

    @Test
    void givenInvalidUserId_whenCreate_thenThrowError() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(UUID.randomUUID(), 10, 3, 30, TIP, null, null);
        NewTrainingPlanDTO newTrainingPlanDTO = new NewTrainingPlanDTO(NAME, List.of(newCustomExerciseDTO), SHORT_DESCRIPTION, LONG_DESCRIPTION, USER_ID, TIP);
        when(userService.findById(USER_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingPlanService.create(newTrainingPlanDTO));

        assertEquals("User with id: " + USER_ID + " not found", exception.getMessage());
        verify(userService).findById(USER_ID);
    }

    @Test
    void givenValidTrainingPlanIdAndNewTrainingPlanDTO_whenUpdate_thenReturnUpdatedTrainingPlan() {
        UUID generalExerciseId = UUID.randomUUID();
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(generalExerciseId, 10, 3, 30, TIP, null, null);
        NewTrainingPlanDTO newTrainingPlanDTO = new NewTrainingPlanDTO(NAME, List.of(newCustomExerciseDTO), SHORT_DESCRIPTION, LONG_DESCRIPTION, USER_ID, TIP);
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();

        when(generalExerciseService.findById(generalExerciseId)).thenReturn(Optional.of(generalExercise));
        when(trainingPlanRepository.findById(TRAINING_PLAN_ID)).thenReturn(Optional.of(trainingPlan));
        when(trainingPlanRepository.save(trainingPlan)).thenReturn(trainingPlan);

        TrainingPlanEntity result = trainingPlanService.update(TRAINING_PLAN_ID, newTrainingPlanDTO);

        assertNotNull(result);
        assertEquals(NAME, result.getName());
        verify(trainingPlanRepository).findById(TRAINING_PLAN_ID);
        verify(trainingPlanRepository).save(trainingPlan);
    }

    @Test
    void givenInvalidTrainingPlanId_whenUpdate_thenThrowException() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(UUID.randomUUID(), 10, 3, 30, TIP, null, null);
        NewTrainingPlanDTO newTrainingPlanDTO = new NewTrainingPlanDTO(NAME, List.of(newCustomExerciseDTO), SHORT_DESCRIPTION, LONG_DESCRIPTION, USER_ID, TIP);
        when(trainingPlanRepository.findById(TRAINING_PLAN_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> trainingPlanService.update(TRAINING_PLAN_ID, newTrainingPlanDTO));

        assertEquals("Training plan with id: " + TRAINING_PLAN_ID + " not found", exception.getMessage());
        verify(trainingPlanRepository).findById(TRAINING_PLAN_ID);
    }

    @Test
    void givenTrainingPlanId_whenDeleteById_thenVerifyDeletion() {
        trainingPlanService.deleteById(TRAINING_PLAN_ID);
        verify(trainingPlanRepository).deleteById(TRAINING_PLAN_ID);
    }
}
