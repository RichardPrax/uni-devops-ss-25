package de.koerperschmiede.backend.controller;

import de.koerperschmiede.backend.mapping.Mapper;
import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import de.koerperschmiede.backend.models.dto.out.GeneralExerciseDTO;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.services.GeneralExerciseService;
import de.koerperschmiede.backend.util.Category;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/general-exercises")
public class GeneralExerciseController {

    private final GeneralExerciseService generalExerciseService;

    @GetMapping
    public ResponseEntity<List<GeneralExerciseDTO>> findAll() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(generalExerciseService.findAll()
                .stream()
                .map(Mapper::generalExerciseEntityToGeneralExerciseDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<GeneralExerciseEntity> optionalGeneralExercise = generalExerciseService.findById(id);

        if (optionalGeneralExercise.isEmpty()) {
            throw new NoSuchElementException("General exercise with id: " + id + " not found");
        }

        GeneralExerciseEntity generalExercise = optionalGeneralExercise.get();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.generalExerciseEntityToGeneralExerciseDTO(generalExercise));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid NewGeneralExerciseDTO newGeneralExerciseDTO) {
        GeneralExerciseEntity newExercise = generalExerciseService.create(newGeneralExerciseDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(Mapper.generalExerciseEntityToGeneralExerciseDTO(newExercise));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody @Valid NewGeneralExerciseDTO newGeneralExerciseDTO) {
        GeneralExerciseEntity updatedGeneralExercise = generalExerciseService.update(id, newGeneralExerciseDTO);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Mapper.generalExerciseEntityToGeneralExerciseDTO(updatedGeneralExercise));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        generalExerciseService.deleteById(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping(path = "/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = Arrays.stream(Category.values())
            .map(Enum::name)
            .collect(Collectors.toList());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(categories);
    }
}
