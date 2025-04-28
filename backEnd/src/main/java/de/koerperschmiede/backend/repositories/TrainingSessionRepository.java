package de.koerperschmiede.backend.repositories;

import de.koerperschmiede.backend.models.entities.TrainingSessionEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainingSessionRepository extends JpaRepository<TrainingSessionEntity, UUID> {
    List<TrainingSessionEntity> findAllByUserId(UUID userId);

    UUID user(UserEntity user);

    Optional<TrainingSessionEntity> findByIdAndUserId(UUID id, UUID userId);
}
