export async function apiFetch<T>(url: string, options?: RequestInit): Promise<T> {
    let jwtToken: string | null = null;

    // Prüfen, ob der Code im Browser läuft
    if (typeof window !== "undefined") {
        jwtToken = localStorage.getItem("token");
    }

    const headers = {
        Authorization: jwtToken ? `Bearer ${jwtToken}` : "",
        "Content-Type": "application/json",
        ...(options?.headers || {}),
    };

    const response = await fetch(url, { ...options, headers });

    if (!response.ok) {
        let errorMessage = "Unknown error";
        try {
            const errorData = await response.json();
            errorMessage = errorData.reason || errorMessage;
        } catch {
            // remain errorMessage if no JSON is returned
        }
        throw new Error(errorMessage);
    }

    return response.json();
}

