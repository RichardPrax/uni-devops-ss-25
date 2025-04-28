import { useState } from "react";
import { apiFetch } from "@/lib/api";
import { GeneralExercise } from "@/app/entities/GeneralExercise";
import { NewGeneralExerciseDTO } from "@/app/entities/NewGeneralExerciseDTO";

export function useUpdateGeneralExercise() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const updateGeneralExercise = async (data: GeneralExercise) => {
        setLoading(true);
        setError(null);

        const newExerciseData: NewGeneralExerciseDTO = {
            name: data.name,
            shortDescription: data.shortDescription,
            longDescription: data.longDescription,
            directions: data.directions,
            categories: data.categories,
            equipment: data.equipment,
            video: data.video,
            thumbnailUrl: data.thumbnailUrl,
        };

        try {
            return await apiFetch(`http://localhost:8080/api/v1/general-exercises/${data.id}`, {
                method: "PUT",
                body: JSON.stringify(newExerciseData),
            });
        } catch (error: any) {
            setError(error.message);
            return null;
        } finally {
            setLoading(false);
        }
    };

    return { updateGeneralExercise, loading, error };
}
