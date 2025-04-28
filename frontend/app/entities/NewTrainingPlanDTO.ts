import { NewCustomExerciseDTO } from "./NewCustomExerciseDTO";

export interface NewTrainingPlanDTO {
    name: string;
    exercises: NewCustomExerciseDTO[];
    shortDescription: string;
    longDescription: string;
    userId: string;
    tip?: string;
}

