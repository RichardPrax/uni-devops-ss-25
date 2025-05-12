import { useState, useEffect } from "react";

import { User } from "@/app/entities/User";
import { apiFetch } from "@/lib/api";

export function useFetchUsers() {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchUsers = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<User[]>(`http://localhost:8080/api/v1/users`, {
                method: "GET",
            });
            setUsers(data);
        } catch (error: unknown) {
            if (error instanceof Error) {
                setError(error.message);
            } else {
                setError("An unknown error occurred");
            }
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    return { users, loading, error, refetch: fetchUsers };
}

