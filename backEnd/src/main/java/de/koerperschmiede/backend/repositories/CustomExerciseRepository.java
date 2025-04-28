package de.koerperschmiede.backend.repositories;

import de.koerperschmiede.backend.models.entities.CustomExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CustomExerciseRepository extends JpaRepository<CustomExerciseEntity, UUID> {
    List<CustomExerciseEntity> findAllByTrainingPlanId(UUID trainingPlanId);
}
