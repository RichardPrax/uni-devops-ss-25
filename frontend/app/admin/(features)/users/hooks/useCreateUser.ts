import { useState } from "react";

import { NewUserDTO } from "@/app/entities/NewUserDTO";
import { apiFetch } from "@/lib/api";

export function useCreateUser() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const createUser = async (userData: NewUserDTO) => {
        setLoading(true);
        setError(null);

        try {
            return await apiFetch("http://localhost:8080/api/v1/auth/register", {
                method: "POST",
                body: JSON.stringify(userData),
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

    return { createUser, loading, error };
}

