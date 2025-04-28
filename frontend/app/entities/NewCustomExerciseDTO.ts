export interface NewCustomExerciseDTO {
    generalExerciseId: string;
    sets: number;
    repetitions: number;
    durationInMinutes: number;
    tip?: string;
    trainingPlanId?: string;
    id?: string;
}

