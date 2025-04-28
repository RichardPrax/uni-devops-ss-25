package de.koerperschmiede.backend.models.entities;

import de.koerperschmiede.backend.util.Category;
import de.koerperschmiede.backend.util.Equipment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "general_exercises")
public class GeneralExerciseEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @ElementCollection(targetClass = Category.class)
    @CollectionTable(name = "exercise_categories", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private List<Category> categories;

    @ElementCollection(targetClass = Equipment.class)
    @CollectionTable(name = "exercise_equipments", joinColumns = @JoinColumn(name = "exercise_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "equipment")
    private List<Equipment> equipment;

    @Column(nullable = false)
    private String shortDescription;

    @Column(nullable = false)
    private String longDescription;

    @Column(nullable = false)
    private String directions;

    @Column(nullable = false)
    private String video;

    @Column(nullable = false, name = "thumbnail_url")
    private String thumbNailUrl;

    public static GeneralExerciseEntity of(String name, List<Category> categories, List<Equipment> equipment, String shortDescription, String longDescription, String directions, String video, String thumbNailUrl) {
        return new GeneralExerciseEntity(name, categories, equipment, shortDescription, longDescription, directions, video, thumbNailUrl);
    }
}
