package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.CustomExerciseRepository;
import de.koerperschmiede.backend.repositories.TrainingPlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CustomExerciseService {

    private final CustomExerciseRepository customExerciseRepository;
    private final GeneralExerciseService generalExerciseService;
    private final TrainingPlanRepository trainingPlanRepository;


    public List<CustomExerciseEntity> findAllByTrainingPlanId(UUID trainingPlanId) {
        return customExerciseRepository.findAllByTrainingPlanId(trainingPlanId);
    }

    public List<CustomExerciseEntity> findAllByTrainingPlanIdForUser(UUID trainingPlanId, UserEntity user) {

        var trainingPlanOptional = trainingPlanRepository.findByUserIdAndId(user.getId(), trainingPlanId);

        if (trainingPlanOptional.isEmpty()) {
            throw new AccessException("You do not have permission to access this resource");
        }

        return trainingPlanOptional.get().getExercises();
    }

    public Optional<CustomExerciseEntity> findById(UUID id) {
        return customExerciseRepository.findById(id);
    }

    public Optional<CustomExerciseEntity> findByIdForUser(UUID id, UserEntity user) {

        var trainingPlans = trainingPlanRepository.findAllByUserId(user.getId());

        List<CustomExerciseEntity> userExercises = trainingPlans.stream()
            .flatMap(trainingPlan -> trainingPlan.getExercises().stream())
            .toList();

        return userExercises.stream()
            .filter(exercise -> exercise.getId().equals(id))
            .findFirst();
    }

    public CustomExerciseEntity createAndSave(NewCustomExerciseDTO newCustomExerciseDTO, TrainingPlanEntity trainingPlan) {
        CustomExerciseEntity newCustomExerciseDBO = buildCustomExerciseEntity(newCustomExerciseDTO, trainingPlan);
        return customExerciseRepository.save(newCustomExerciseDBO);
    }

    public CustomExerciseEntity create(NewCustomExerciseDTO newCustomExerciseDTO, TrainingPlanEntity trainingPlan) {
        return buildCustomExerciseEntity(newCustomExerciseDTO, trainingPlan);
    }

    private CustomExerciseEntity buildCustomExerciseEntity(NewCustomExerciseDTO newCustomExerciseDTO, TrainingPlanEntity trainingPlan) {
        GeneralExerciseEntity generalExercise = getGeneralExercise(newCustomExerciseDTO.generalExerciseId());

        return CustomExerciseEntity.of(
            generalExercise,
            trainingPlan,
            newCustomExerciseDTO.repetitions(),
            newCustomExerciseDTO.sets(),
            newCustomExerciseDTO.durationInMinutes(),
            newCustomExerciseDTO.tip()
        );
    }

    public CustomExerciseEntity update(UUID id, NewCustomExerciseDTO newCustomExerciseDTO) {
        CustomExerciseEntity customExercise = getCustomExercise(id);
        GeneralExerciseEntity generalExercise = getGeneralExercise(newCustomExerciseDTO.generalExerciseId());

        updateCustomExerciseDetails(customExercise, newCustomExerciseDTO, generalExercise);
        return customExerciseRepository.save(customExercise);
    }

    private void updateCustomExerciseDetails(CustomExerciseEntity customExercise, NewCustomExerciseDTO newCustomExerciseDTO, GeneralExerciseEntity generalExercise) {
        customExercise.setTip(newCustomExerciseDTO.tip());
        customExercise.setGeneralExercise(generalExercise);
        customExercise.setRepetitions(newCustomExerciseDTO.repetitions());
        customExercise.setSets(newCustomExerciseDTO.sets());
        customExercise.setDurationInMinutes(newCustomExerciseDTO.durationInMinutes());
    }

    public void deleteById(UUID id) {
        customExerciseRepository.deleteById(id);
    }

    private CustomExerciseEntity getCustomExercise(UUID id) {
        return customExerciseRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Custom exercise with id: " + id + " not found"));
    }

    private GeneralExerciseEntity getGeneralExercise(UUID id) {
        return generalExerciseService.findById(id)
            .orElseThrow(() -> new NoSuchElementException("General exercise with id: " + id + " not found"));
    }
}
