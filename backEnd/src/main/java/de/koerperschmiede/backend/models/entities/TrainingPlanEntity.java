package de.koerperschmiede.backend.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "training_plans")
public class TrainingPlanEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomExerciseEntity> exercises = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String longDescription;

    @Column(nullable = false)
    private String tip;

    public static TrainingPlanEntity of(String name, List<CustomExerciseEntity> exercises, UserEntity user, String shortDescription, String longDescription, String tip) {
        return new TrainingPlanEntity(name, exercises, user, shortDescription, longDescription, tip);
    }
}
