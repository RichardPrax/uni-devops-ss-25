import { GeneralExercise } from "@/app/entities/GeneralExercise";
import { useState, useEffect } from "react";
import { apiFetch } from "@/lib/api";

export function useFetchGeneralExercise(id: string) {
    const [exercise, setExercise] = useState<GeneralExercise>();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchGeneralExercise = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<GeneralExercise>(`http://localhost:8080/api/v1/general-exercises/${id}`, {
                method: "GET",
            });
            setExercise(data);
        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGeneralExercise();
    }, [id]);

    return { exercise, loading, error, refetch: fetchGeneralExercise };
}
