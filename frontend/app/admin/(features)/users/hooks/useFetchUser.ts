import { useState, useEffect, useCallback } from "react";

import { User } from "@/app/entities/User";
import { apiFetch } from "@/lib/api";

export function useFetchUser(userId: string) {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchUser = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<User>(`http://localhost:8080/api/v1/users/${userId}`, {
                method: "GET",
            });
            setUser(data);
        } catch (error: unknown) {
            if (error instanceof Error) {
                setError(error.message);
            } else {
                setError("An unknown error occurred");
            }
        } finally {
            setLoading(false);
        }
    }, [userId]);

    useEffect(() => {
        fetchUser();
    }, [userId, fetchUser]);

    return { user, loading, error, refetch: fetchUser };
}

