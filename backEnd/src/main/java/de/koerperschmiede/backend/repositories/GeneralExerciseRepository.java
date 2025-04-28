package de.koerperschmiede.backend.repositories;

import de.koerperschmiede.backend.models.entities.GeneralExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GeneralExerciseRepository extends JpaRepository<GeneralExerciseEntity, UUID> {
}
