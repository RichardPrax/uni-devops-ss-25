import { CustomExercise } from "./CustomExercise";

export interface TrainingPlan {
    id: string;
    name: string;
    exercises: CustomExercise[];
    longDescription: string;
    shortDescription: string;
    tip: string;
}
