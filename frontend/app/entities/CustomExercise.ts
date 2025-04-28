import { GeneralExercise } from "./GeneralExercise";

export interface CustomExercise {
    id: string;
    generalExercise: GeneralExercise;
    repetitions: number;
    sets: number;
    durationInMinutes: number;
    tip: string;
}
