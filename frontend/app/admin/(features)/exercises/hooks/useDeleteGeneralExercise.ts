import { useState } from "react";
import { apiFetch } from "@/lib/api";

export function useDeleteGeneralExercise() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const deleteGeneralExercise = async (id: string) => {
        setLoading(true);
        setError(null);

        try {
            return await apiFetch(`http://localhost:8080/api/v1/general-exercises/${id}`, {
                method: "DELETE",
            });
        } catch (error: any) {
            setError(error.message);
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { deleteGeneralExercise, loading, error };
}
