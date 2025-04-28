package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.mapping.Mapper;
import de.koerperschmiede.backend.models.dto.in.NewCustomExerciseDTO;
import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.services.AuthenticationService;
import de.koerperschmiede.backend.services.CustomExerciseService;
import de.koerperschmiede.backend.services.TrainingPlanService;
import de.koerperschmiede.backend.util.Role;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/custom-exercises")
public class CustomExerciseController {
    private final CustomExerciseService customExerciseService;
    private final TrainingPlanService trainingPlanService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<?>> findAllByTrainingPlanId(@RequestParam UUID trainingPlanId) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var customExercises = customExerciseService.findAllByTrainingPlanId(trainingPlanId);
            var customExerciseDTOs = customExercises
                .stream()
                .map(Mapper::customExerciseEntityToCustomExerciseDTO)
                .toList();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(customExerciseDTOs);
        }

        var customExercises = customExerciseService.findAllByTrainingPlanIdForUser(trainingPlanId, user);
        var customExerciseDTOs = customExercises.stream()
            .map(Mapper::customExerciseEntityToCustomExerciseDTO)
            .toList();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(customExerciseDTOs);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var customExerciseOptional = customExerciseService.findById(id);
            if (customExerciseOptional.isEmpty()) {
                throw new NoSuchElementException("Custom exercise with id: " + id + " not found");
            }
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.customExerciseEntityToCustomExerciseDTO(customExerciseOptional.get()));
        }

        var customExerciseOptional = customExerciseService.findByIdForUser(id, user);
        if (customExerciseOptional.isEmpty()) {
            throw new AccessException("You do not have permission to access this resource");
        }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.customExerciseEntityToCustomExerciseDTO(customExerciseOptional.get()));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewCustomExerciseDTO newCustomExerciseDTO) {
        Optional<TrainingPlanEntity> optionalTrainingPlan = trainingPlanService.findById(newCustomExerciseDTO.trainingPlanId());

        if (optionalTrainingPlan.isEmpty()) {
            throw new NoSuchElementException("Training plan with id: " + newCustomExerciseDTO.trainingPlanId() + " not found");
        }

        TrainingPlanEntity trainingPlan = optionalTrainingPlan.get();
        CustomExerciseEntity customExerciseDBO = customExerciseService.createAndSave(newCustomExerciseDTO, trainingPlan);
        trainingPlanService.addExercise(trainingPlan, customExerciseDBO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Mapper.customExerciseEntityToCustomExerciseDTO(customExerciseDBO));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid NewCustomExerciseDTO newCustomExerciseDTO) {
        CustomExerciseEntity updatedCustomExercise = customExerciseService.update(id, newCustomExerciseDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.customExerciseEntityToCustomExerciseDTO(updatedCustomExercise));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        customExerciseService.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
