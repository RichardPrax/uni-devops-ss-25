import { GeneralExercise } from "@/app/entities/GeneralExercise";
import { useState, useEffect } from "react";
import { apiFetch } from "@/lib/api";

export function useFetchGeneralExercises() {
    const [exercises, setExercises] = useState<GeneralExercise[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchGeneralExercises = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<GeneralExercise[]>("http://localhost:8080/api/v1/general-exercises", {
                method: "GET",
            });
            setExercises(data);
        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchGeneralExercises();
    }, []);

    return { exercises, loading, error, refetch: fetchGeneralExercises };
}
