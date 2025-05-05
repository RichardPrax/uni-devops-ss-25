package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.models.dto.in.NewGeneralExerciseDTO;
import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import de.koerperschmiede.backend.repositories.GeneralExerciseRepository;
import de.koerperschmiede.backend.util.Category;
import de.koerperschmiede.backend.util.Equipment;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GeneralExerciseService {

    private final GeneralExerciseRepository generalExerciseRepository;

    public List<GeneralExerciseEntity> findAll() {
        return generalExerciseRepository.findAll();
    }

    public Optional<GeneralExerciseEntity> findById(UUID id) {
        return generalExerciseRepository.findById(id);
    }

    public GeneralExerciseEntity create(NewGeneralExerciseDTO newGeneralExerciseDTO) {
        List<Category> categories = mapEnumValues(newGeneralExerciseDTO.categories(), Category::valueOf);
        List<Equipment> equipment = mapEnumValues(newGeneralExerciseDTO.equipment(), Equipment::valueOf);

        GeneralExerciseEntity newGeneralExerciseDBO = GeneralExerciseEntity.of(
            newGeneralExerciseDTO.name(),
            categories,
            equipment,
            newGeneralExerciseDTO.shortDescription(),
            newGeneralExerciseDTO.longDescription(),
            newGeneralExerciseDTO.directions(),
            newGeneralExerciseDTO.video(),
            newGeneralExerciseDTO.thumbnailUrl()
        );
        return generalExerciseRepository.save(newGeneralExerciseDBO);
    }

    public GeneralExerciseEntity update(UUID id, NewGeneralExerciseDTO newGeneralExerciseDTO) {
        GeneralExerciseEntity generalExercise = getGeneralExercise(id);

        List<Category> categories = mapEnumValues(newGeneralExerciseDTO.categories(), Category::valueOf);
        List<Equipment> equipment = mapEnumValues(newGeneralExerciseDTO.equipment(), Equipment::valueOf);

        updateGeneralExerciseDetails(newGeneralExerciseDTO, generalExercise, categories, equipment);

        return generalExerciseRepository.save(generalExercise);
    }

    private void updateGeneralExerciseDetails(NewGeneralExerciseDTO newGeneralExerciseDTO,
        GeneralExerciseEntity generalExercise,
        List<Category> categories,
        List<Equipment> equipment) {
        generalExercise.setName(newGeneralExerciseDTO.name());
        generalExercise.setCategories(categories);
        generalExercise.setEquipment(equipment);
        generalExercise.setShortDescription(newGeneralExerciseDTO.shortDescription());
        generalExercise.setLongDescription(newGeneralExerciseDTO.longDescription());
        generalExercise.setDirections(newGeneralExerciseDTO.directions());
        generalExercise.setVideo(newGeneralExerciseDTO.video());
        generalExercise.setThumbNailUrl(newGeneralExerciseDTO.thumbnailUrl());
    }

    public void deleteById(UUID id) {
        generalExerciseRepository.deleteById(id);
    }

    private <T extends Enum<T>> List<T> mapEnumValues(List<String> values, Function<String, T> mapper) {
        return values.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    private GeneralExerciseEntity getGeneralExercise(UUID id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("General exercise with id " + id + " not found"));
    }
}
