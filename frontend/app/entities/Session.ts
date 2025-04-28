import { TrainingPlan } from "./TrainingPlan";

export interface Session {
    id: string;
    trainingPlan: TrainingPlan;
    date: string;
    notes: string;
}
