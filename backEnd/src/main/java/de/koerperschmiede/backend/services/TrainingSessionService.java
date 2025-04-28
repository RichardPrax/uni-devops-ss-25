package de.koerperschmiede.backend.services;

import de.koerperschmiede.backend.exceptions.AccessException;
import de.koerperschmiede.backend.models.dto.in.NewTrainingSessionDTO;
import de.koerperschmiede.backend.models.entities.TrainingPlanEntity;
import de.koerperschmiede.backend.models.entities.TrainingSessionEntity;
import de.koerperschmiede.backend.models.entities.UserEntity;
import de.koerperschmiede.backend.repositories.TrainingSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingPlanService trainingPlanService;
    private final UserService userService;

    public List<TrainingSessionEntity> findAllByUserId(UUID userId) {
        return trainingSessionRepository.findAllByUserId(userId);
    }

    public Optional<TrainingSessionEntity> findById(UUID id) {
        return trainingSessionRepository.findById(id);
    }

    public Optional<TrainingSessionEntity> findByIdForUser(UUID id, UserEntity user) {
        return trainingSessionRepository.findByIdAndUserId(id, user.getId());
    }

    public TrainingSessionEntity create(NewTrainingSessionDTO newTrainingSessionDTO) {
        UserEntity user = getUserById(newTrainingSessionDTO.userId());
        TrainingPlanEntity trainingPlan = getTrainingPlanById(newTrainingSessionDTO.trainingPlanId());

        validateUserTrainingPlan(user, trainingPlan);

        TrainingSessionEntity sessionEntity = TrainingSessionEntity.of(
            trainingPlan,
            user,
            newTrainingSessionDTO.date(),
            newTrainingSessionDTO.notes()
        );

        return trainingSessionRepository.save(sessionEntity);
    }

    public TrainingSessionEntity update(UUID id, NewTrainingSessionDTO newTrainingSessionDTO) {
        TrainingSessionEntity session = getSessionById(id);

        session.setNotes(newTrainingSessionDTO.notes());
        session.setDate(newTrainingSessionDTO.date());
        return trainingSessionRepository.save(session);
    }

    public void deleteById(UUID id) {
        trainingSessionRepository.deleteById(id);
    }

    public void deleteByIdForUser(UUID id, UserEntity user) {
        Optional<TrainingSessionEntity> optionalSession = trainingSessionRepository.findByIdAndUserId(id, user.getId());

        if (optionalSession.isEmpty()) {
            throw new AccessException("You do not have permission to access this resource");
        }

        trainingSessionRepository.deleteById(id);
    }

    private UserEntity getUserById(UUID userId) {
        return userService.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " not found"));
    }

    private TrainingPlanEntity getTrainingPlanById(UUID trainingPlanId) {
        return trainingPlanService.findById(trainingPlanId)
            .orElseThrow(() -> new NoSuchElementException("Training plan with id: " + trainingPlanId + " not found"));
    }

    private void validateUserTrainingPlan(UserEntity user, TrainingPlanEntity trainingPlan) {
        if (!user.getId().equals(trainingPlan.getUser().getId())) {
            throw new IllegalArgumentException("User ID: " + user.getId() + " does not match the user ID: " + trainingPlan.getUser().getId() + " of the training plan");
        }
    }

    private TrainingSessionEntity getSessionById(UUID id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Training session with id: " + id + " not found"));
    }
}
