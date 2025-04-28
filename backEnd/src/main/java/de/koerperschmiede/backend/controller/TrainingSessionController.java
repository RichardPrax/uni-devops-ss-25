package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.mapping.Mapper;
import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.services.AuthenticationService;
import de.koerperschmiede.backend.services.TrainingSessionService;
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
@RequestMapping(path = "/api/v1/training-sessions")
public class TrainingSessionController {

    private final TrainingSessionService trainingSessionService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<?>> findAllByUserId(@RequestParam UUID userId) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {

            var trainingSessions = trainingSessionService.findAllByUserId(userId);

            var trainingSessionDTOs = trainingSessions
                .stream()
                .map(Mapper::sessionEntityToSessionDTO)
                .toList();

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(trainingSessionDTOs);
        }

        if (!(user.getId().equals(userId))) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var trainingSessions = trainingSessionService.findAllByUserId(userId);
        var trainingSessionDTOs = trainingSessions
            .stream()
            .map(Mapper::sessionEntityToSessionDTO)
            .toList();

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(trainingSessionDTOs);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var optionalSession = trainingSessionService.findById(id);

            if (optionalSession.isEmpty()) {
                throw new NoSuchElementException("Training session with id: " + id + " not found");
            }

            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.sessionEntityToSessionDTO(optionalSession.get()));
        }

        var optionalSession = trainingSessionService.findByIdForUser(id, user);

        if (optionalSession.isEmpty()) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var session = optionalSession.get();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.sessionEntityToSessionDTO(session));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewTrainingSessionDTO newTrainingSessionDTO) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var newTrainingSession = trainingSessionService.create(newTrainingSessionDTO);

            return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Mapper.sessionEntityToSessionDTO(newTrainingSession));
        }

        if (!(user.getId().equals(newTrainingSessionDTO.userId()))) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var newTrainingSession = trainingSessionService.create(newTrainingSessionDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Mapper.sessionEntityToSessionDTO(newTrainingSession));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid NewTrainingSessionDTO newTrainingSessionDTO) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            var updatedTrainingSession = trainingSessionService.update(id, newTrainingSessionDTO);
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(Mapper.sessionEntityToSessionDTO(updatedTrainingSession));
        }

        if (!(user.getId().equals(newTrainingSessionDTO.userId()))) {
            throw new AccessException("You do not have permission to access this resource");
        }

        var updatesTrainingSession = trainingSessionService.update(id, newTrainingSessionDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.sessionEntityToSessionDTO(updatesTrainingSession));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {

        UserEntity user = authenticationService.getUserEntity();

        if (user.getRole().equals(Role.ADMIN)) {
            trainingSessionService.deleteById(id);
            return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
        }

        trainingSessionService.deleteByIdForUser(id, user);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }
}
