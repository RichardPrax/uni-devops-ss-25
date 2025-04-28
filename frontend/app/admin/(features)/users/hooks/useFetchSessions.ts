import { Session } from "@/app/entities/Session";
import { useState, useEffect } from "react";
import { apiFetch } from "@/lib/api";

export function useFetchSessions(userId: string) {
    const [sessions, setSessions] = useState<Session[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const fetchSessions = async () => {
        setLoading(true);
        setError(null);

        try {
            const data = await apiFetch<Session[]>(`http://localhost:8080/api/v1/training-sessions?userId=${userId}`, {
                method: "GET",
            });
            setSessions(data);
        } catch (error: any) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchSessions();
    }, [userId]);

    return { sessions, loading, error, refetch: fetchSessions };
}
