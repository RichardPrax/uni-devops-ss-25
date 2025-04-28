package de.koerperschmiede.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "training_sessions")
public class TrainingSessionEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id", nullable = false)
    private TrainingPlanEntity trainingPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false)
    private String notes;

    public static TrainingSessionEntity of(TrainingPlanEntity trainingPlan, UserEntity user, Instant date, String notes) {
        return new TrainingSessionEntity(trainingPlan, user, date, notes);
    }
}
