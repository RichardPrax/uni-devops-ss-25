package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.repositories.CustomExerciseRepository;
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

class CustomExerciseServiceTest {

    private static final UUID CUSTOM_EXERCISE_ID = UUID.randomUUID();
    private static final UUID GENERAL_EXERCISE_ID = UUID.randomUUID();
    private static final UUID TRAINING_PLAN_ID = UUID.randomUUID();
    private static final int REPETITIONS = 10;
    private static final int SETS = 3;
    private static final int DURATION_IN_MINUTES = 30;
    private static final String TIP = "Keep your back straight";

    @Mock
    private CustomExerciseRepository customExerciseRepository;

    @Mock
    private GeneralExerciseService generalExerciseService;

    @InjectMocks
    private CustomExerciseService customExerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenCustomExercisesExist_whenFindAllByTrainingPlanId_thenReturnAllCustomExercisesForUser() {
        List<CustomExerciseEntity> customExercises = List.of(new CustomExerciseEntity(), new CustomExerciseEntity());
        when(customExerciseRepository.findAllByTrainingPlanId(TRAINING_PLAN_ID)).thenReturn(customExercises);

        List<CustomExerciseEntity> result = customExerciseService.findAllByTrainingPlanId(TRAINING_PLAN_ID);

        assertEquals(customExercises.size(), result.size());
        verify(customExerciseRepository).findAllByTrainingPlanId(TRAINING_PLAN_ID);
    }

    @Test
    void givenValidCustomExerciseId_whenFindById_thenReturnCustomExercise() {
        CustomExerciseEntity customExercise = new CustomExerciseEntity();
        when(customExerciseRepository.findById(CUSTOM_EXERCISE_ID)).thenReturn(Optional.of(customExercise));

        Optional<CustomExerciseEntity> result = customExerciseService.findById(CUSTOM_EXERCISE_ID);

        assertTrue(result.isPresent());
        verify(customExerciseRepository).findById(CUSTOM_EXERCISE_ID);
    }

    @Test
    void givenInvalidCustomExerciseId_whenFindById_thenReturnEmptyOptional() {
        when(customExerciseRepository.findById(CUSTOM_EXERCISE_ID)).thenReturn(Optional.empty());

        Optional<CustomExerciseEntity> result = customExerciseService.findById(CUSTOM_EXERCISE_ID);

        assertTrue(result.isEmpty());
        verify(customExerciseRepository).findById(CUSTOM_EXERCISE_ID);
    }

    @Test
    void givenNewCustomExerciseDTO_whenCreateAndSave_thenReturnCreatedCustomExercise() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(GENERAL_EXERCISE_ID, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP, null, null);
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();
        CustomExerciseEntity customExercise = new CustomExerciseEntity(generalExercise, trainingPlan, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP);

        when(generalExerciseService.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.of(generalExercise));
        when(customExerciseRepository.save(any(CustomExerciseEntity.class))).thenReturn(customExercise);

        CustomExerciseEntity result = customExerciseService.createAndSave(newCustomExerciseDTO, trainingPlan);

        assertNotNull(result);
        assertEquals(REPETITIONS, result.getRepetitions());
        verify(generalExerciseService).findById(GENERAL_EXERCISE_ID);
        verify(customExerciseRepository).save(any(CustomExerciseEntity.class));
    }

    @Test
    void givenNewCustomExerciseDTOWithInvalidGeneralExerciseId_whenCreateAndSave_thenThrowError() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(GENERAL_EXERCISE_ID, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP, null, null);
        TrainingPlanEntity trainingPlan = new TrainingPlanEntity();
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();
        CustomExerciseEntity customExercise = new CustomExerciseEntity(generalExercise, trainingPlan, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP);

        when(generalExerciseService.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.empty());
        when(customExerciseRepository.save(any(CustomExerciseEntity.class))).thenReturn(customExercise);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customExerciseService.createAndSave(newCustomExerciseDTO, trainingPlan));

        assertEquals("General exercise with id: " + GENERAL_EXERCISE_ID + " not found", exception.getMessage());
        verify(generalExerciseService).findById(GENERAL_EXERCISE_ID);
    }

    @Test
    void givenValidCustomExerciseIdAndNewCustomExerciseDTO_whenUpdate_thenReturnUpdatedCustomExercise() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(GENERAL_EXERCISE_ID, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP, null, null);
        CustomExerciseEntity customExercise = new CustomExerciseEntity();
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();

        when(customExerciseRepository.findById(CUSTOM_EXERCISE_ID)).thenReturn(Optional.of(customExercise));
        when(generalExerciseService.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.of(generalExercise));
        when(customExerciseRepository.save(customExercise)).thenReturn(customExercise);

        CustomExerciseEntity result = customExerciseService.update(CUSTOM_EXERCISE_ID, newCustomExerciseDTO);

        assertNotNull(result);
        assertEquals(REPETITIONS, result.getRepetitions());
        verify(customExerciseRepository).findById(CUSTOM_EXERCISE_ID);
        verify(generalExerciseService).findById(GENERAL_EXERCISE_ID);
        verify(customExerciseRepository).save(customExercise);
    }

    @Test
    void givenInvalidCustomExerciseId_whenUpdate_thenThrowException() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(GENERAL_EXERCISE_ID, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP, null, null);
        when(customExerciseRepository.findById(CUSTOM_EXERCISE_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customExerciseService.update(CUSTOM_EXERCISE_ID, newCustomExerciseDTO));

        assertEquals("Custom exercise with id: " + CUSTOM_EXERCISE_ID + " not found", exception.getMessage());
        verify(customExerciseRepository).findById(CUSTOM_EXERCISE_ID);
    }

    @Test
    void givenInvalidGeneralExerciseId_whenUpdate_thenThrowException() {
        NewCustomExerciseDTO newCustomExerciseDTO = new NewCustomExerciseDTO(GENERAL_EXERCISE_ID, REPETITIONS, SETS, DURATION_IN_MINUTES, TIP, null, null);
        CustomExerciseEntity customExercise = new CustomExerciseEntity();
        when(customExerciseRepository.findById(CUSTOM_EXERCISE_ID)).thenReturn(Optional.of(customExercise));
        when(generalExerciseService.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> customExerciseService.update(CUSTOM_EXERCISE_ID, newCustomExerciseDTO));

        assertEquals("General exercise with id: " + GENERAL_EXERCISE_ID + " not found", exception.getMessage());
        verify(generalExerciseService).findById(GENERAL_EXERCISE_ID);
    }

    @Test
    void givenCustomExerciseId_whenDeleteById_thenVerifyDeletion() {
        customExerciseService.deleteById(CUSTOM_EXERCISE_ID);
        verify(customExerciseRepository).deleteById(CUSTOM_EXERCISE_ID);
    }
}
