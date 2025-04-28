package de.koerperschmiede.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "custom_exercises")
public class CustomExerciseEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "general_exercise_id")
    private GeneralExerciseEntity generalExercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_plan_id", nullable = false)
    private TrainingPlanEntity trainingPlan;

    @Column(nullable = false)
    private int repetitions;

    @Column(nullable = false)
    private int sets;

    @Column(nullable = false)
    private int durationInMinutes;

    @Column(nullable = false)
    private String tip;

    public static CustomExerciseEntity of(GeneralExerciseEntity generalExercise, TrainingPlanEntity trainingPlan, int repetitions, int sets, int durationInMinutes, String tip) {
        return new CustomExerciseEntity(generalExercise, trainingPlan, repetitions, sets, durationInMinutes, tip);
    }
}
