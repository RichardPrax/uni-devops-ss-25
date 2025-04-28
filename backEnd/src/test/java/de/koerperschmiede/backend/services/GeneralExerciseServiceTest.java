package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.repositories.GeneralExerciseRepository;
import de.koerperschmiede.backend.util.Category;
import de.koerperschmiede.backend.util.Equipment;
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

class GeneralExerciseServiceTest {

    private static final UUID GENERAL_EXERCISE_ID = UUID.randomUUID();
    private static final String NAME = "Squat";
    private static final String NEW_NAME = "BackSquat";
    private static final List<String> CATEGORIES = List.of("LOWER_BODY");
    private static final List<Category> CATEGORIES_ENUM = List.of(Category.LOWER_BODY);
    private static final List<String> EQUIPMENT = List.of("CHAIR");
    private static final List<Equipment> EQUIPMENT_ENUM = List.of(Equipment.CHAIR);
    private static final String SHORT_DESCRIPTION = "Short description";
    private static final String LONG_DESCRIPTION = "Long description";
    private static final String DIRECTIONS = "Directions";
    private static final String VIDEO = "Video";
    private static final String THUMBNAIL_URL = "Thumbnail URL";

    @Mock
    private GeneralExerciseRepository generalExerciseRepository;

    @InjectMocks
    private GeneralExerciseService generalExerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenGeneralExercisesExist_whenFindAll_thenReturnAllGeneralExercises() {
        List<GeneralExerciseEntity> generalExercises = List.of(new GeneralExerciseEntity(), new GeneralExerciseEntity());
        when(generalExerciseRepository.findAll()).thenReturn(generalExercises);

        List<GeneralExerciseEntity> result = generalExerciseService.findAll();

        assertEquals(generalExercises.size(), result.size());
        verify(generalExerciseRepository).findAll();
    }

    @Test
    void givenValidGeneralExerciseId_whenFindById_thenReturnGeneralExercise() {
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();
        when(generalExerciseRepository.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.of(generalExercise));

        Optional<GeneralExerciseEntity> result = generalExerciseService.findById(GENERAL_EXERCISE_ID);

        assertTrue(result.isPresent());
        verify(generalExerciseRepository).findById(GENERAL_EXERCISE_ID);
    }

    @Test
    void givenInvalidGeneralExerciseId_whenFindById_thenReturnEmptyOptional() {
        when(generalExerciseRepository.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.empty());

        Optional<GeneralExerciseEntity> result = generalExerciseService.findById(GENERAL_EXERCISE_ID);

        assertTrue(result.isEmpty());
        verify(generalExerciseRepository).findById(GENERAL_EXERCISE_ID);
    }

    @Test
    void givenNewGeneralExerciseDTO_whenCreate_thenReturnCreatedGeneralExercise() {
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(NAME, CATEGORIES, EQUIPMENT, SHORT_DESCRIPTION, LONG_DESCRIPTION, DIRECTIONS, VIDEO, THUMBNAIL_URL);
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity(NAME, CATEGORIES_ENUM, EQUIPMENT_ENUM, SHORT_DESCRIPTION, LONG_DESCRIPTION, DIRECTIONS, VIDEO, THUMBNAIL_URL);
        when(generalExerciseRepository.save(any(GeneralExerciseEntity.class))).thenReturn(generalExercise);

        GeneralExerciseEntity result = generalExerciseService.create(newGeneralExerciseDTO);

        assertNotNull(result);
        assertEquals(NAME, result.getName());
        verify(generalExerciseRepository).save(any(GeneralExerciseEntity.class));
    }

    @Test
    void givenValidGeneralExerciseIdAndNewGeneralExerciseDTO_whenUpdate_thenReturnUpdatedGeneralExercise() {
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(NEW_NAME, CATEGORIES, EQUIPMENT, SHORT_DESCRIPTION, LONG_DESCRIPTION, DIRECTIONS, VIDEO, THUMBNAIL_URL);
        GeneralExerciseEntity generalExercise = new GeneralExerciseEntity();
        when(generalExerciseRepository.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.of(generalExercise));
        when(generalExerciseRepository.save(generalExercise)).thenReturn(generalExercise);

        GeneralExerciseEntity result = generalExerciseService.update(GENERAL_EXERCISE_ID, newGeneralExerciseDTO);

        assertNotNull(result);
        assertEquals(NEW_NAME, result.getName());
        verify(generalExerciseRepository).findById(GENERAL_EXERCISE_ID);
        verify(generalExerciseRepository).save(generalExercise);
    }

    @Test
    void givenInvalidGeneralExerciseId_whenUpdate_thenThrowException() {
        NewGeneralExerciseDTO newGeneralExerciseDTO = new NewGeneralExerciseDTO(NEW_NAME, CATEGORIES, EQUIPMENT, SHORT_DESCRIPTION, LONG_DESCRIPTION, DIRECTIONS, VIDEO, THUMBNAIL_URL);
        when(generalExerciseRepository.findById(GENERAL_EXERCISE_ID)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> generalExerciseService.update(GENERAL_EXERCISE_ID, newGeneralExerciseDTO));

        assertEquals("General exercise with id " + GENERAL_EXERCISE_ID + " not found", exception.getMessage());
        verify(generalExerciseRepository).findById(GENERAL_EXERCISE_ID);
    }

    @Test
    void givenGeneralExerciseId_whenDeleteById_thenVerifyDeletion() {
        generalExerciseService.deleteById(GENERAL_EXERCISE_ID);
        verify(generalExerciseRepository).deleteById(GENERAL_EXERCISE_ID);
    }
}
