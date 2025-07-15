import { useState } from "react";

import { NewGeneralExerciseDTO } from "@/app/entities/NewGeneralExerciseDTO";
import { apiFetch } from "@/lib/api";

export function useCreateGeneralExercise() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const createGeneralExercise = async (data: NewGeneralExerciseDTO) => {
        setLoading(true);
        setError(null);

        try {
            return await apiFetch("/general-exercises", {
                method: "POST",
                body: JSON.stringify(data),
            });
        } catch (error: unknown) {
            if (error instanceof Error) {
                setError(error.message);
            } else {
                setError("An unknown error occurred");
            }
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { createGeneralExercise, loading, error };
}

