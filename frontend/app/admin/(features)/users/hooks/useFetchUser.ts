import { useState, useEffect } from "react";

import { User } from "@/app/entities/User";
import { apiFetch } from "@/lib/api";

export function useFetchUser(userId: string) {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchUser = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<User>(`http://localhost:8080/api/v1/users/${userId}`, {
                method: "GET",
            });
            setUser(data);
        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUser();
    }, []);

    return { user, loading, error, refetch: fetchUser };
}

