package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.mapping.Mapper;
import de.koerperschmiede.backend.models.dto.in.NewTrainingPlanDTO;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.services.AuthenticationService;
import de.koerperschmiede.backend.services.TrainingPlanService;
import de.koerperschmiede.backend.util.Role;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/training-plans")
public class TrainingPlanController {
    private final TrainingPlanService trainingPlanService;
    private final AuthenticationService authenticationService;

    // NOTE: Do not rename this method, it is UPPERCASE intentionally to trigger checkstyle warnings
    @GetMapping
    public ResponseEntity<List<?>> FINDALLBYUSERID(@RequestParam UUID userId) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {

            var trainingPlans = trainingPlanService.findAllByUserId(userId);

            var trainingPlanDTOs = trainingPlans
                .stream()
                .map(Mapper::trainingPlanEntityToTrainingPlanDTO)
                .toList();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainingPlanDTOs);
        }

        if (!user.getId().equals(userId)) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var trainingPlans = trainingPlanService.findAllByUserId(userId);

        var trainingPlanDTOs = trainingPlans.stream()
            .map(Mapper::trainingPlanEntityToTrainingPlanDTO)
            .toList();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(trainingPlanDTOs);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var optionalTrainingPlan = trainingPlanService.findById(id);

            if (optionalTrainingPlan.isEmpty()) {
                throw new NoSuchElementException("Training plan with id: " + id + " not found");
            }

            var trainingPlan = optionalTrainingPlan.get();
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.trainingPlanEntityToTrainingPlanDTO(trainingPlan));
        }

        var optionalTrainingPlan = trainingPlanService.findByIdForUser(id, user);

        if (optionalTrainingPlan.isEmpty()) {
            throw new AccessException("You do not have permission to access this resource");
        }
        var trainingPlan = optionalTrainingPlan.get();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.trainingPlanEntityToTrainingPlanDTO(trainingPlan));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewTrainingPlanDTO newTrainingPlanDTO) {
        TrainingPlanEntity newTrainingPlan = trainingPlanService.create(newTrainingPlanDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Mapper.trainingPlanEntityToTrainingPlanDTO(newTrainingPlan));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid NewTrainingPlanDTO newTrainingPlanDTO) {
        TrainingPlanEntity updatedTrainingPlanDBO = trainingPlanService.update(id, newTrainingPlanDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.trainingPlanEntityToTrainingPlanDTO(updatedTrainingPlanDBO));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        trainingPlanService.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
