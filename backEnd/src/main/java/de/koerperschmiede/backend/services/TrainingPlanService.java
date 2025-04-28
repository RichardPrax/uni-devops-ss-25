package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TrainingPlanRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserService userService;
    private final CustomExerciseService customExerciseService;
    private final GeneralExerciseService generalExerciseService;

    public List<TrainingPlanEntity> findAllByUserId(UUID userId) {
        return trainingPlanRepository.findAllByUserId(userId);
    }

    public Optional<TrainingPlanEntity> findById(UUID id) {
        return trainingPlanRepository.findById(id);
    }

    public Optional<TrainingPlanEntity> findByIdForUser(UUID id, UserEntity user) {
        return trainingPlanRepository.findByUserIdAndId(user.getId(), id);
    }

    public TrainingPlanEntity create(NewTrainingPlanDTO newTrainingPlanDTO) {
        UserEntity user = getUser(newTrainingPlanDTO.userId());

        // create training plan
        TrainingPlanEntity trainingPlan = TrainingPlanEntity.of(
            newTrainingPlanDTO.name(),
            new ArrayList<>(),
            user,
            newTrainingPlanDTO.shortDescription(),
            newTrainingPlanDTO.longDescription(),
            newTrainingPlanDTO.tip()
        );

        for (NewCustomExerciseDTO newCustomExercise : newTrainingPlanDTO.exercises()) {
            CustomExerciseEntity customExercise = customExerciseService.create(newCustomExercise, trainingPlan);
            trainingPlan.getExercises().add(customExercise);
        }

        return trainingPlanRepository.save(trainingPlan);
    }


    @Transactional
    public TrainingPlanEntity update(UUID id, NewTrainingPlanDTO newTrainingPlanDTO) {
        TrainingPlanEntity trainingPlan = getTrainingPlan(id);
        updateTrainingPlanDetails(trainingPlan, newTrainingPlanDTO);

        Map<UUID, CustomExerciseEntity> existingExerciseMap = trainingPlan.getExercises().stream()
            .collect(Collectors.toMap(CustomExerciseEntity::getId, e -> e));

        List<CustomExerciseEntity> updatedExercises = newTrainingPlanDTO.exercises().stream()
            .map(exerciseDTO -> updateOrCreateExercise(exerciseDTO, existingExerciseMap, trainingPlan))
            .toList();

        existingExerciseMap.values().forEach(exercise -> customExerciseService.deleteById(exercise.getId()));

        // Important: Use clear() + addAll() because Hibernate uses proxy lists!
        // setExercises(new ArrayList<>(...)) would break the Hibernate relationship.
        trainingPlan.getExercises().clear();
        trainingPlan.getExercises().addAll(updatedExercises);


        return trainingPlanRepository.save(trainingPlan);
    }

    private void updateTrainingPlanDetails(TrainingPlanEntity trainingPlan, NewTrainingPlanDTO dto) {
        trainingPlan.setName(dto.name());
        trainingPlan.setShortDescription(dto.shortDescription());
        trainingPlan.setLongDescription(dto.longDescription());
        trainingPlan.setTip(dto.tip());
    }

    private CustomExerciseEntity updateOrCreateExercise(NewCustomExerciseDTO exerciseDTO, Map<UUID, CustomExerciseEntity> existingExerciseMap, TrainingPlanEntity trainingPlan) {
        if (exerciseDTO.id() != null && existingExerciseMap.containsKey(exerciseDTO.id())) {
            CustomExerciseEntity exercise = existingExerciseMap.remove(exerciseDTO.id());
            updateExistingExercise(exercise, exerciseDTO);
            return exercise;
        }
        return createNewExercise(exerciseDTO, trainingPlan);
    }

    private void updateExistingExercise(CustomExerciseEntity exercise, NewCustomExerciseDTO dto) {
        exercise.setSets(dto.sets());
        exercise.setRepetitions(dto.repetitions());
        exercise.setDurationInMinutes(dto.durationInMinutes());
        exercise.setTip(dto.tip());
    }

    private CustomExerciseEntity createNewExercise(NewCustomExerciseDTO dto, TrainingPlanEntity trainingPlan) {
        GeneralExerciseEntity generalExercise = getGeneralExercise(dto.generalExerciseId());
        return CustomExerciseEntity.of(
            generalExercise, trainingPlan, dto.repetitions(), dto.sets(), dto.durationInMinutes(), dto.tip()
        );
    }

    public void deleteById(UUID id) {
        trainingPlanRepository.deleteById(id);
    }

    public void addExercise(TrainingPlanEntity trainingPlan, CustomExerciseEntity customExerciseDBO) {
        trainingPlan.getExercises().add(customExerciseDBO);
    }

    private UserEntity getUser(UUID id) {
        return userService.findById(id)
            .orElseThrow(() -> new NoSuchElementException("User with id: " + id + " not found"));
    }

    private TrainingPlanEntity getTrainingPlan(UUID id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Training plan with id: " + id + " not found"));
    }

    private GeneralExerciseEntity getGeneralExercise(UUID id) {
        return generalExerciseService.findById(id)
            .orElseThrow(() -> new NoSuchElementException("General exercise with id: " + id + " not found"));
    }
}
