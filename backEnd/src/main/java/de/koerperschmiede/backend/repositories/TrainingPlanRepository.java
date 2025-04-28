package de.koerperschmiede.backend.repositories;

import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlanEntity, UUID> {
    List<TrainingPlanEntity> findAllByUserId(UUID userId);

    // TODO: ist sowas sinnvoll? oder auch im programmcode lieber selbst darum kümmern
    Optional<TrainingPlanEntity> findByUserIdAndId(UUID userId, UUID id);
}
