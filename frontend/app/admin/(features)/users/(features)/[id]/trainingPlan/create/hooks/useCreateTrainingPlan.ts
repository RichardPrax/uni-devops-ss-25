import { useState } from "react";
import { apiFetch } from "@/lib/api";
import { NewTrainingPlanDTO } from "@/app/entities/NewTrainingPlanDTO";

export function useCreateTrainingPlan() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const createTrainingPlan = async (data: NewTrainingPlanDTO) => {
        setLoading(true);
        setError(null);

        try {
            return await apiFetch("http://localhost:8080/api/v1/training-plans", {
                method: "POST",
                body: JSON.stringify(data),
            });
        } catch (error: any) {
            setError(error.message);
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { createTrainingPlan, loading, error };
}
