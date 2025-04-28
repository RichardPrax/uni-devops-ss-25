import { TrainingPlan } from "@/app/entities/TrainingPlan";
import { useState, useEffect } from "react";
import { apiFetch } from "@/lib/api";

export function useFetchTrainingPlans(userId: string) {
    const [trainingPlans, setTrainingPlans] = useState<TrainingPlan[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchTrainingPlans = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<TrainingPlan[]>(`http://localhost:8080/api/v1/training-plans?userId=${userId}`, {
                method: "GET",
            });
            setTrainingPlans(data);
        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchTrainingPlans();
    }, [userId]);

    return { trainingPlans, loading, error, refetch: fetchTrainingPlans };
}
